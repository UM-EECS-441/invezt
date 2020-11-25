from django.shortcuts import render
from django.http import JsonResponse, HttpResponse
from django.db import connection
from django.views.decorators.csrf import csrf_exempt
import json
from google.oauth2 import id_token
from google.auth.transport import requests
import stripe

import hashlib, time

stripe.api_key = "sk_test_51HmWsQLCRJXQF7TjecAKzl9UPOg37FAGvPmYh1DNbuN4kUz2yJunTO1mMeAPoNEtrtikfw9IOgWNiZOHdsf7cnIj00vkn65vLZ"

stripe.api_version = "2020-08-27"

@csrf_exempt
def patterns_bought_GET(request, inveztid):
    if request.method != 'GET':
        return HttpResponse(status='404')
    cursor = connection.cursor()
    cursor.execute('''SELECT pattern_name
                    FROM purchases
                    WHERE inveztid = "{}"
                    UNION
                    SELECT pattern_name
                    FROM patterns
                    WHERE price = 0
                    ORDER BY pattern_name;'''.format(inveztid))
    rows = cursor.fetchall()
    response = {}
    response['inveztid'] = inveztid
    patterns = []
    for row in rows:
        patterns.append(row[0])
        response['patterns'] = patterns
    return JsonResponse(response)


@csrf_exempt
def patterns_bought_POST(request):
    if request.method != 'POST':
        return HttpResponse(status='404')
    json_data = json.loads(request.body)
    inveztid = json_data['inveztid']
    patterns = json_data['patterns']
    for pattern in patterns:
        cursor = connection.cursor()
        cursor.execute('INSERT INTO purchases (inveztid, pattern_name) VALUES ("{}","{}");'.format(inveztid, pattern))
    return JsonResponse({})


def patterns_list(request):
    if request.method != 'GET':
        return HttpResponse(status=404)
    cursor = connection.cursor()
    cursor.execute('SELECT pattern_name FROM patterns ORDER BY pattern_name;')
    rows = cursor.fetchall()
    response = {}
    patterns = []
    for row in rows:
        patterns.append(row[0])
    response['patterns'] = patterns
    return JsonResponse(response)


@csrf_exempt
def pattern_articles(request):
    if request.method != 'GET':
        return HttpResponse(status=404)
    cursor = connection.cursor()
    cursor.execute('SELECT * FROM patterns ORDER BY pattern_name;')
    rows = cursor.fetchall()
    response = {}
    for row in rows:
        article = {}
        article['pattern_name'] = row[0]
        article['image'] = row[1]
        article['description'] = row[2]
        article['wiki_link'] = row[3]
        article['price'] = row[4]
        response[row[0]] = article
    return JsonResponse(response)


def pattern_draw(request):
    if request.method != 'GET':
        return HttpResponse(status=404)
    #cursor = connection.cursor()
    #cursor.execute('SELECT * FROM patterns ORDER BY pattern_name;')
    #rows = cursor.fetchall()
    #response = {}
    #response['articles'] = rows
    #return JsonResponse(response)
    return 'UNDER CONSTRUCTION'

@csrf_exempt
def add_user(request):
    if request.method != 'POST':
        return HttpResponse(status=404)

    json_data = json.loads(request.body)
    clientID = json_data['clientID']   # the front end app's OAuth 2.0 Client ID
    idToken = json_data['idToken']     # user's OpenID ID Token, a JSon Web Token (JWT)

    currentTimeStamp = time.time()
    backendSecret = "ifyougiveamouse"

    try:
        # Collect user info from the Google idToken, verify_oauth2_token checks
        # the integrity of idToken and throws a "ValueError" if idToken or
        # clientID is corrupted or if user has been disconnected from Google
        # OAuth (requiring user to log back in to Google).
        idinfo = id_token.verify_oauth2_token(idToken, requests.Request(), clientID)

        # Verify the token is valid and fresh
        if idinfo['iss'] not in ['accounts.google.com', 'https://accounts.google.com']:
            raise ValueError('Wrong issuer.')
        if currentTimeStamp >= idinfo['exp']:
            raise ValueError('Expired token.')

    except ValueError:
        # Invalid or expired token
        return HttpResponse(status=511)  # 511 Network Authentication Required

    # Check if token already exists in database
    # Instead of the unlimited length ID Token,
    # we work with a fixed-size SHA256 of the ID Token.
    tokenhash = hashlib.sha256(idToken.strip().encode('utf-8')).hexdigest()

    cursor = connection.cursor()
    cursor.execute("SELECT inveztid, stripeid FROM users WHERE idtoken ='"+ tokenhash + "';")

    row = cursor.fetchone()
    if row is not None:
        response = {}
        response['inveztID'] = row[0]
        response['stripeID'] = row[1]
        # if we've already seen the token, return associated inveztID
        return JsonResponse(response)

    # If it's a new token, get username
    try:
        username = idinfo['name']
    except:
        username = "Profile NA"

    # Compute inveztID and add to database
    hashable = idToken + username + str(currentTimeStamp) + backendSecret
    inveztID = hashlib.sha256(hashable.strip().encode('utf-8')).hexdigest()
    # Create new Stripe customer
    response = stripe.Customer.create()
    stripeID = response["id"]
    # Add a new user to the database
    cursor.execute('INSERT INTO users (inveztid, idtoken, username, stripeid) VALUES '
                   '(%s, %s, %s, %s);', (inveztID, tokenhash, username, stripeID))

    # Return inveztID
    response = {}
    response['inveztID'] = inveztID
    response['stripeID'] = stripeID
    return JsonResponse(response)


def create_stripe_key(request, inveztid, api_version):
    if request.method != 'GET':
        return HttpResponse(status='404')

    cursor = connection.cursor()
    cursor.execute('SELECT stripeid FROM users WHERE inveztid="{}";'.format(inveztid))
    stripeid = cursor.fetchone()[0]

    key = stripe.EphemeralKey.create(customer=stripeid, stripe_version=api_version)

    return JsonResponse(key)

@csrf_exempt
def create_stripe_paymentIntent(request):
    if request.method != 'POST':
        return HttpResponse(status='404')

    json_data = json.loads(request.body)

    # Get list of patterns
    patterns = json_data['patterns']
    stripeID = json_data['stripeID']
    payment_methodID = json_data['paymentMethodID']

    patterns = str(patterns)

    # Remove the [ ]
    patterns = patterns[1:-1]

    # We now have something like "pattern1", "pattern2", ...

    price = 0.0

    cursor = connection.cursor()
    cursor.execute('SELECT price FROM patterns WHERE pattern_name IN ({});'.format(patterns))

    rows = cursor.fetchall()
    
    for row in rows:
        price = price + row[0]

    # Convert dollars to cents
    price = int(price * 100)

    intent = stripe.PaymentIntent.create(
            amount=price,
            currency='usd',
            customer=stripeID,
            payment_method=payment_methodID
            )
   
    payment_intentID = intent.id

    return JsonResponse({'payment_intentID': payment_intentID})


@csrf_exempt
def confirm_payment(request):
    if request.method != 'POST':
        return HttpResponse(status='404')

    json_data = json.loads(request.body)

    payment_intentID = json_data['payment_intentID']

    # Confirm payment
    results = stripe.PaymentIntent.confirm(payment_intentID)

    return JsonResponse({'status': results.status})

# Add a pattern to a user's cart
@csrf_exempt
def add_to_cart(request):
    if request.method != 'POST':
        return HttpResponse(status='404')

    json_data = json.loads(request.body)
    inveztid = json_data["inveztid"]
    pattern_name = json_data["pattern_name"]

    cursor = connection.cursor()
    cursor.execute('''INSERT INTO carts(inveztid, pattern_name)
                      VALUES("{}","{}");'''.format(inveztid, pattern_name))

    return JsonResponse({})


# Get the cart for a given user
def get_cart(request, inveztid):
    if request.method != 'GET':
        return HttpResponse(status='404')

    cursor = connection.cursor()
    cursor.execute('''SELECT pattern_name
                      FROM carts
                      WHERE inveztid="{}"
                      ORDER BY pattern_name;'''.format(inveztid))

    rows = cursor.fetchall()
    
    patterns = []

    for row in rows:
        patterns.append(row[0])

    response = {}
    response["patterns"] = patterns
    return JsonResponse(response)


# Remove a pattern from the cart
@csrf_exempt
def remove_from_cart(request):
    if request.method != 'POST':
        return HttpResponse(status='404')

    json_data = json.loads(request.body)
    inveztid = json_data["inveztid"]
    patterns = json_data["patterns"]
    patterns = str(patterns)
    patterns = patterns[1:-1]

    cursor = connection.cursor()
    cursor.execute('''DELETE FROM carts
                      WHERE inveztid="{}"
                      AND pattern_name IN ({});'''.format(inveztid, patterns))

    return JsonResponse({})
