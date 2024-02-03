package com.example.linstagram.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.linstagram.R
import com.example.linstagram.databinding.ActivitySignUpBinding
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var binding:ActivitySignUpBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth


       binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)


        binding.signup.setOnClickListener(){
            sign_up_button_clicked(binding.editText1.text.toString(),binding.editText3.text.toString())
        }





    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }

    private fun reload() {

    }


    fun sign_up_button_clicked(email: String, password:String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAGK", "createUserWithEmail:success")
                    val user = auth.currentUser
                    val profileUpdates = userProfileChangeRequest {
                        displayName = binding.editText2.text.toString()
                    }

                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("TAGK", "User profile updated.")
                            }
                        }
                    updateUI()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAGK", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Probably your mail adress not valid or password must be more than 5 digit",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
    }





    private fun updateUI() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }


    //Passwordless

    fun createlink(email: String){

        val auth = Firebase.auth
        val user = auth.currentUser!!
        val url = "http://www.example.com/verify?uid=" + user.uid
        val actionCodeSettings = actionCodeSettings {

            handleCodeInApp = true
            setIOSBundleId("com.example.ios")
            setAndroidPackageName(
                "com.example.android",
                true, // installIfNotAvailable
                "12", // minimumVersion
            )
        }
    }


    fun sendlink(email:String,actionCodeSettings: ActionCodeSettings){
        Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAGK", "Email sent")
                }
            }
    }


    fun completesigning(email: String){
        val auth = Firebase.auth
        val intent = intent
        val emailLink = intent.data.toString()

        // Confirm the link is a sign-in with email link.
        if (auth.isSignInWithEmailLink(emailLink)) {
            // Retrieve this from wherever you stored it
            val email = email

            // The client SDK will parse the code from the link for you.
            auth.signInWithEmailLink(email, emailLink)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("TAGK", "Successfully signed in with email link!")
                        val result = task.result
                        // You can access the new user via result.getUser()
                        // Additional user info profile *not* available via:
                        // result.getAdditionalUserInfo().getProfile() == null
                        // You can check if the user is new or existing:
                        // result.getAdditionalUserInfo().isNewUser()
                    } else {
                        Log.e("TAGK", "Error signing in with email link", task.exception)
                    }
                }
        }
    }
}