package com.example.cmbss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.cmbss.ui.theme.CmbssTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AddPostActivity : AppCompatActivity() , AddPostCallBack {
    val addPostCallBack: AddPostCallBack =this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CmbssTheme {
                AddPostScreen(addPostCallBack)
            }
        }
    }

    override fun OnPost(username: String,title: String, description: String) {
        sentfirebase(username,title,description)
        val intent= Intent(this@AddPostActivity,studenthomeActivity::class.java)
        startActivity(intent)
    }

    fun sentfirebase(postId: String, title:String,description: String){

// Reference to the Firestore database
        val db = Firebase.firestore

// Reference to the "posts" collection
        val postsCollection = db.collection("posts")

// Create a new job post
        val jobPost = JobPost(
            id=postId,
            title = title,
            description = description
        )
        val jodid=postId
        postsCollection
            .document(jodid) // Assuming "job" is the document ID
            .set(jobPost)
            .addOnSuccessListener {
                // Document added successfully
            }
            .addOnFailureListener { e ->
                // Error adding document
            }
    }
}