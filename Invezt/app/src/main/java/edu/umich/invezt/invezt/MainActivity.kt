package edu.umich.invezt.invezt

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import edu.umich.invezt.invezt.MainActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.Size
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.stripe.android.CustomerSession
import com.stripe.android.EphemeralKeyProvider
import com.stripe.android.EphemeralKeyUpdateListener
import edu.umich.invezt.invezt.App.Companion.inveztID
import edu.umich.invezt.invezt.App.Companion.stripeID


class MainActivity : AppCompatActivity() {
    companion object {
        val stripe_api_version = "2020-08-27"
    }

    var idToken: String? = null
    var googleID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set the tokenID and googleID
        idToken = intent.getStringExtra("IDTOKEN")
        googleID = intent.getStringExtra("GOOGLEID")

        // If the user signed in, grab the inveztID from the backend
        if (idToken != null) {
            addUser()
        }
        // Else, continue without being logged in
        else {
            inveztID = null
        }
    }

// Navigates to ScanActivity
    fun toScan(view: View?) {
        val intent = Intent(this, ScanActivity::class.java)
        startActivity(intent)
    }

// Navigates to LearnActivity
    fun toLearn(view: View?) {
        val intent = Intent(this, LearnActivity::class.java)
        startActivity(intent)
    }

    // Handles adding a user to the database, or getting the inveztID if the user is already registered
    private fun addUser() {
        // Signed-in user hasn't been checked in the backend yet
        if (inveztID == null) {
            val queue = Volley.newRequestQueue(this)

            // obtain inveztID from backend
            val url = "https://167.71.176.115/add_user/"
            val params = mapOf(
                "clientID" to getString(R.string.clientID),
                "idToken" to idToken,
                "googleID" to googleID
            )

            val postRequest = JsonObjectRequest(url, JSONObject(params),
                { response ->
                    try {
                        // Store the inveztID and stripeID
                        inveztID = response.getString("inveztID")
                        stripeID = response.getString("stripeID")

                        // User registration failed
                        if (inveztID == null) {
                            toast("Error adding user. Please try again.")
                            setContentView(R.layout.activity_signin)
                        }
                        // Create a Stripe Customer Session for the user
                        CustomerSession.initCustomerSession(this, MyEphemeralKeyProvider())


                    } catch (e: JSONException) {
                        // user registration failed.
                        toast("Server Error.")
                        // Return to sign-in page
                        setContentView(R.layout.activity_signin)
                    }
                },
                { // user registration failed.  Try again:
                    toast("Authentication Error. Please try again.")
                    // Return to sign-in page
                    setContentView(R.layout.activity_signin)
                }
            )
            queue.add(postRequest)
        }
    }

    // Sign the user out when the app closes
    override protected fun onDestroy() {
        super.onDestroy()
        setContentView(R.layout.activity_main)

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build()

        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mGoogleSignInClient.signOut()
            .addOnCompleteListener(this) {
                // If the user was signed in, tell them they are now signed out
                if (inveztID != null) {
                    toast("Signed out")
                    // Reset the inveztid
                    inveztID = null
                }
                finish()
            }

        // End Stripe Customer Section
        CustomerSession.endCustomerSession()
    }

    // Navigates to Cart Activity
    fun goToCart(view: View?) {
        val intent : Intent = Intent(this, CartActivity::class.java)
        startActivity(intent)
    }

    inner class MyEphemeralKeyProvider : EphemeralKeyProvider {
        val url = "https://167.71.176.15/create_stripe_key/${inveztID}/$stripe_api_version/"

        override fun createEphemeralKey(
            @Size(min = 4) apiVersion: String,
            keyUpdateListener: EphemeralKeyUpdateListener
        ) {
            val queue = Volley.newRequestQueue(this@MainActivity)
            val url = "https://167.71.176.115/create_stripe_key/${inveztID}/$stripe_api_version/"

            val getRequest = JsonObjectRequest(url, null,
                { response ->
                    keyUpdateListener.onKeyUpdate(response.toString())
                },
                { error ->
                    keyUpdateListener.onKeyUpdateFailure(0, error.message ?: "")
                }
            )

            queue.add(getRequest)
        }
    }

}