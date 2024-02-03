package com.example.linstagram.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.linstagram.R
import com.example.linstagram.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.linstagram.model.JournalUser

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.signupMain.setOnClickListener(){
            val i = Intent(this, SignUpActivity::class.java)
            startActivity(i)
        }


        auth = Firebase.auth

        binding.login.setOnClickListener(){
            LoginWithEmailandPassw(
                binding.usernameInput.text.toString().trim(),
                binding.passwordInput.text.toString().trim()
            )
        }

    }

    private fun LoginWithEmailandPassw(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {


                    var journal = JournalUser.instanceUser!!

                    journal.userId = auth.currentUser?.uid.toString()

                    journal.username = auth.currentUser?.displayName.toString()




                    Log.d("TAGK", "signInWithEmail:success,${journal.username+journal.userId}")
                    val user = auth.currentUser
                    goToJournalListActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAGK", "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }


    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        Log.w("LOGK","SAVEPOINTMAINACT")
        if (currentUser != null) {
            Log.w("LOGK","SAVEPOINT2MAINACT")
            goToJournalListActivity()
        }
    }

    private fun goToJournalListActivity() {
        var i = Intent(this, JournalListActivity::class.java)
        startActivity(i)
    }


}