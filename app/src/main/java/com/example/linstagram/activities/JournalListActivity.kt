package com.example.linstagram.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View.VISIBLE
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.linstagram.model.Journal
import com.example.linstagram.adapter.MyAdapter
import com.example.linstagram.R
import com.example.linstagram.databinding.ActivityJournalListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.StorageReference

class JournalListActivity : AppCompatActivity() {

    lateinit var binding : ActivityJournalListBinding


    //Firebase
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var user:FirebaseUser
    val db = FirebaseFirestore.getInstance()
    lateinit var storageReference: StorageReference
    var collectionReference:CollectionReference = db.collection("journal")





    lateinit var journal_list:MutableList<Journal>
    lateinit var recyclerAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal_list)

        journal_list = arrayListOf<Journal>()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_journal_list)

        //Firebase Initialize
        firebaseAuth = Firebase.auth
        user = firebaseAuth.currentUser!!



        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)



        val toolbar: Toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false)
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when(item.itemId) {
                R.id.action_add -> if (user != null && firebaseAuth != null) {
                    val inte = Intent(this, AddJournalActivity::class.java)
                    startActivity(inte)
                }

                R.id.action_signout -> if (user != null && firebaseAuth != null) {
                    firebaseAuth.signOut()
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        val storageRef = Firebase.storage.reference

        storageRef.child("journal_images/my_image").getBytes(Long.MAX_VALUE).addOnSuccessListener {
            // Use the bytes to display the image
        }.addOnFailureListener {
            // Handle any errors
        }





        Log.w("TAGK","LISTACTIVITYSAVEPOINT1")
        collectionReference.whereEqualTo("userId",
            user.uid)
            .get()
            .addOnSuccessListener {
                Log.w("TAGK","LISTACTIVITYSAVEPOINT2${it.size()}")
                if(!it.isEmpty){
                    Log.w("TAGK","LISTACTIVITYSAVEPOINT3")
                    for (document in it) {
                        var journal = Journal(
                            document.data.get("tit").toString(),
                            document.data.get("descrp").toString(),
                            document.data.get("imageUrl").toString(),
                            document.data.get("userId").toString(),
                            document.data.get("timeAdded").toString(),
                            document.data.get("username").toString())
                        journal_list.add(journal)
                    }
                    Log.w("TAGK","LISTACTIVITYSAVEPOINT4")
                    // RecyclerView
                    recyclerAdapter = MyAdapter(
                        this,
                        journal_list
                    )
                    binding.recyclerView.adapter = recyclerAdapter
                    recyclerAdapter.notifyDataSetChanged()



                    Log.w("TAGK","LISTACTIVITYSAVEPOINT5")

                }
                else{
                    binding.textView.visibility = VISIBLE
                    Log.w("TAGK","LISTACTIVITYSAVEPOINT6")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAGK", "Error getting documents: ", exception)
            }


    }
}