package com.example.linstagram.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.linstagram.model.Journal
import com.example.linstagram.model.JournalUser
import com.example.linstagram.R
import com.example.linstagram.databinding.ActivityAddJournalBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AddJournalActivity : AppCompatActivity() {


    var currentuserId = ""
    var currentuserName = ""


    lateinit var auth: FirebaseAuth
    lateinit var user:FirebaseUser

    lateinit var binding:ActivityAddJournalBinding

    val db = Firebase.firestore

    var storageRef = Firebase.storage.reference

    var collectionReference = db.collection("journal")

    lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_journal)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_journal)

        auth = Firebase.auth

        binding.apply {
            if(JournalUser.instanceUser != null){


                currentuserId = auth.currentUser?.uid.toString()
                currentuserName = auth.currentUser?.displayName.toString()
                Log.w("TAGK", "AddJournalActivitySAVEPOINT1${currentuserName}")
                titleDisplay.text = currentuserName
            }
            postCameraButton.setOnClickListener(){
                var i= Intent(Intent.ACTION_GET_CONTENT)
                i.setType("image/*")
                startActivityForResult(i,1)
            }


            postitbutton.setOnClickListener(){
                SaveJournal()
            }
        }


    }

    private fun SaveJournal() {
        var title = binding.titleInput.text.toString().trim()
        var thoughts = binding.thoughts.text.toString().trim()
        binding.reload.visibility = VISIBLE

        var timestamp = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        // Format the LocalDateTime using the formatter


        val formattedDateTime: String = timestamp.format(formatter)

        if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(thoughts) && imageUri != null) {
            var tme = Timestamp.now().seconds
            var imagesRef: StorageReference? = storageRef.child("journal_images")
                .child("my_image" + tme)
            imagesRef?.putFile(imageUri)
                ?.addOnSuccessListener {
                    Log.w("TAGK", "ANOTHER SAVE POINT 1")
                    imagesRef?.downloadUrl?.addOnSuccessListener {
                        Log.w("TAGK","ANOTHER SAVE POINT 2")
                        var imageUri = it.toString()


                        var journal= Journal(
                            title,
                            thoughts,
                            //Achtung, my image is Int!!
                            imageUri,
                            currentuserId,
                            formattedDateTime,
                            currentuserName
                        )

                        Log.w("TAGK", "BEFORE DB COLLECTION")
                        db.collection("journal")
                            .add(journal)
                            .addOnSuccessListener { documentReference ->
                                Log.d(
                                    "TAGK",
                                    "DocumentSnapshot written with ID: ${documentReference}"
                                )
                                binding.reload.visibility = INVISIBLE
                                var i = Intent(this, JournalListActivity::class.java)
                                startActivity(i)
                                finish()
                            }.addOnFailureListener{
                                e -> Log.w("TAGK", "ERROR ADDING DOCUMENT", e)
                            }
                    }

                }?.addOnFailureListener { e ->
                    Log.w("TAGK", "Error in the up", e)
                    // YOU WILL GET IMAGE PATH TOMORROW!! <---- Reminder ---->
                    binding.reload.visibility = INVISIBLE
                }

        }else{
            binding.reload.visibility = INVISIBLE
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == RESULT_OK){
            if(data != null){
                imageUri = data.data!!
                binding.postCameraButton.setImageURI(imageUri)

            }
        }
    }

    override fun onStart() {
        super.onStart()

        user = auth.currentUser!!
    }


    override fun onStop() {
        super.onStop()
        if(auth != null){
            //Could add some activity
        }
    }
}


