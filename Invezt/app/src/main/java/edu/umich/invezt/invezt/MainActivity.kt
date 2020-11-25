package edu.umich.invezt.invezt

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MainActivity : AppCompatActivity() {
    companion object {
        var inveztID: String? = null
    }

    var idToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set the tokenID
        idToken = intent.getStringExtra("IDTOKEN")

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
                "idToken" to idToken
            )

            val postRequest = JsonObjectRequest(url, JSONObject(params),
                { response ->
                    try {
                        // Store the inveztID
                        inveztID = response.getString("inveztID")

                        // User registration failed
                        if (inveztID == null) {
                            toast("Error adding user. Please try again.")
                            setContentView(R.layout.activity_signin)
                        }
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
                }
                finish()
            }
    }

}