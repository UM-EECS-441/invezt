package edu.umich.invezt.invezt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.stripe.android.*
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.model.PaymentIntent
import com.stripe.android.model.PaymentMethod
import kotlin.Exception
import edu.umich.invezt.invezt.App.Companion.inveztID
import edu.umich.invezt.invezt.App.Companion.stripeID
import kotlinx.android.synthetic.main.activity_cart.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CartActivity : AppCompatActivity() {

    private lateinit var paymentSession: PaymentSession
    private var clientSecret : String = ""
    private var paymentMethodId : String = ""
    private var paymentIntentID : String = ""
    private var isPaymentReadyToCharge : Boolean = false
    private var cart : JSONArray = JSONArray()
    lateinit var stripe : Stripe
    val requestcode = 4242

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make sure the user is signed in
        if (inveztID == null) {
            toast("Please Sign in")
            return
        }

        // TODO: REMOVE HARD CODED CART FOR TESTING
        cart = JSONArray("[\"Channel\", \"Elliott Wave\"]")
        val shouldbechannel = cart[0]
        val shouldbeelliot = cart[1]
        stripe = Stripe(this, "pk_test_51HmWsQLCRJXQF7TjzWSdvAYoFkfqCFI4VleesPx0zfZSLnGPh45MCg6S4A5NchWLwCzamErq01luAde21KDAZdBB00SW6uNNBI", stripeID)

        setContentView(R.layout.activity_cart)

        paymentSession = PaymentSession(
            this,
            createPaymentSessionConfig()
        )

        setupPaymentSession()

        // Create Recycler View for list of patterns
        try {
            recyclerView.layoutManager =  LinearLayoutManager(this@CartActivity)
            recyclerView.adapter = PatternsAdapter(cart)

        } catch (e: JSONException) {

            setContentView(R.layout.empty_cart)
        }
    }

    private fun createPaymentSessionConfig(): PaymentSessionConfig {
        return PaymentSessionConfig.Builder()
            .setPaymentMethodTypes(
                listOf(PaymentMethod.Type.Card)
            )
            .setShippingInfoRequired(false)
            .setShippingMethodsRequired(false)
            .build()
    }

    private fun setupPaymentSession() {
        // Setup the listener to detect changes in the payment process
        paymentSession.init(
            object : PaymentSession.PaymentSessionListener {
                override fun onCommunicatingStateChanged(isCommunicating: Boolean) {
                    // update UI, such as hiding or showing a progress bar
                }

                override fun onError(errorCode: Int, errorMessage: String) {
                    // handle error
                }

                override fun onPaymentSessionDataChanged(data: PaymentSessionData) {
                    val paymentMethod: PaymentMethod? = data.paymentMethod
                    if (paymentMethod != null) {
                        paymentMethodId = paymentMethod.id.toString()
                    }
                    isPaymentReadyToCharge = data.isPaymentReadyToCharge
                }
            }
        )
    }

    // Run when the "Select a Payment Method" button is clicked
    fun selectPaymentMethod(view: View?) {
        // Let the user select a card or add a new one
        paymentSession.presentPaymentMethodSelection()

        // TODO: possibly update the UI to show the selected payment method
    }

    // Run when the "Purchase" button is clicked
    fun purchaseOnClick(view: View?) {
        // If the payment is ready to be made
        if (isPaymentReadyToCharge) {
            // Make the charge
            startPayment()
        }

        // If the user isn't ready to make the purchase
        else {
            toast("Please select a payment method.")
        }
    }

    private fun startPayment() {
        // Get the Stripe client secret for the transaction
        val queue = Volley.newRequestQueue(this)
        val url = "https://167.71.176.115/create_stripe_paymentIntent/"
        val params = mapOf(
            "patterns" to cart,
            "stripeID" to stripeID,
            "paymentMethodID" to paymentMethodId
        )

        val postRequest = JsonObjectRequest(url, JSONObject(params),
            { response ->
                // Get the payment intent ID
                paymentIntentID = response.getString("payment_intentID")
                // Confirm the payment
                confirmPayment()
            },
            { error ->
                toast(error.message.toString())
            }
        )
        queue.add(postRequest)
    }

    private fun confirmPayment() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://167.71.176.115/confirm_payment/"
        val params = mapOf(
            "payment_intentID" to paymentIntentID
        )

        val postRequest = JsonObjectRequest(url, JSONObject(params),
            {response ->
                val status = response.getString("status")

                if (status == "succeeded") {
                    toast("Purchase successful!")
                    // Add the pattern(s) to the user's list of purchased patterns
                    addToPurchases(inveztID!!, cart)

                    // Remove the pattern from the user's cart
                    removeFromCart(inveztID!!, cart)
                }

                else {
                    toast("Error completing payment")
                }

            },
            {
                toast("Error contacting API")
            })

        queue.add(postRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            paymentSession.handlePaymentData(requestCode, resultCode, data)
        }
    }

    private fun addToPurchases(inveztID: String, patterns: JSONArray) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://167.71.176.115/patterns_bought/"

        val params = mapOf(
            "inveztid" to inveztID,
            "patterns" to patterns
        )

        val postRequest = JsonObjectRequest(url, JSONObject(params),
            {
                toast("Patterns added to your account.")
            },
            {
                toast("ERROR: Patterns not added to your account. Please contact support.")
            })
        queue.add(postRequest)
    }

    // Removes the pattern from the given user's cart
    private fun removeFromCart(inveztid: String, patterns: JSONArray) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://167.71.176.115/remove_from_cart/"
        val params = mapOf(
            "inveztid" to inveztid,
            "patterns" to patterns
        )

        val postRequest = JsonObjectRequest(url, JSONObject(params),
            {
                toast("Patterns removed from Cart")
                /*Possibly update UI to show that pattern is no longer in cart*/
            },
            {
                toast("Failed removing pattern from cart.")
            })

        queue.add(postRequest)
    }


    // Gets the cart for a given user
    private fun getCart(inveztid: String) {
        val queue = Volley.newRequestQueue(this)
        val url = "https://167.71.176.115/get_cart/${inveztid}/"

        val getRequest = JsonObjectRequest(url, null,
            { response ->
                // holds the list of patterns in this user's cart
                cart = response.getJSONArray("patterns")
            },
            {
                toast("Error getting your cart.")
            })

        queue.add(getRequest)
    }
}