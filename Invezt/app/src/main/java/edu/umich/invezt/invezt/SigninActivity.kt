package edu.umich.invezt.invezt

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import edu.umich.invezt.invezt.R.string.clientID
import kotlinx.android.synthetic.main.activity_signin.*

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

class SigninActivity : AppCompatActivity() {
    var RC_SIGN_IN = 0
    var RC_MAIN = 1
    var mGoogleSignInClient: GoogleSignInClient? = null
    var idToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        // Configure sign-in to request the user's ID and basic profile, which are
        // all included in DEFAULT_SIGN_IN.  Email address, however,
        // must be additionally requested with .requestEmail(/*void*/).  Similarly
        // ID Token must be additionally requested, as shown.
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(clientID))
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set the size of the signinButton
        signinButton.setSize(SignInButton.SIZE_STANDARD)
        // and call signIn() when the button is clicked
        signinButton.setOnClickListener(View.OnClickListener {
            signIn()
        })
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient?.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, return user's IdToken
            val resultIntent = Intent(this, MainActivity::class.java)
            toast("Signed in as " + account!!.displayName)
            idToken = account!!.idToken
            resultIntent.putExtra("IDTOKEN", idToken)
            setResult(Activity.RESULT_OK, resultIntent)
            startActivity(resultIntent)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.

            // SignIn or user registration failed.  Try again:
            toast("Error signing in. Please try again.")
            // User must use the back button to cancel sign in.
        }
    }

    public fun skipSignIn(view: View?) {
        // Sign the user out if they're signed in and trying to continue without signing in
        if (idToken != null) {
            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .build()

            val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
            mGoogleSignInClient.signOut()
            idToken = null
        }

        // Tell the user they're not signed in
        toast("Not signed in")

        // Start the main activity
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
    }
}