from django.shortcuts import render
from django.http import JsonResponse, HttpResponse
from django.db import connection
from django.views.decorators.csrf import csrf_exempt
import json
from google.oauth2 import id_token
from google.auth.transport import requests

import hashlib, time

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
    pattern_name = json_data['pattern_name']
    cursor = connection.cursor()
    cursor.execute('INSERT INTO purchases (inveztid, pattern_name) VALUES ("{}","{}");'.format(inveztid, pattern_name))
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
    cursor.execute("SELECT inveztid FROM users WHERE idtoken ='"+ tokenhash + "';")

    inveztID = cursor.fetchone()
    if inveztID is not None:
        # if we've already seen the token, return associated inveztID
        return JsonResponse({'inveztID': inveztID[0]})

    # If it's a new token, get username
    try:
        username = idinfo['name']
    except:
        username = "Profile NA"

    # Compute inveztID and add to database
    hashable = idToken + username + str(currentTimeStamp) + backendSecret
    inveztID = hashlib.sha256(hashable.strip().encode('utf-8')).hexdigest()
    cursor.execute('INSERT INTO users (inveztid, idtoken, username) VALUES '
                   '(%s, %s, %s);', (inveztID, tokenhash, username))

    # Return inveztID
    return JsonResponse({'inveztID': inveztID})
