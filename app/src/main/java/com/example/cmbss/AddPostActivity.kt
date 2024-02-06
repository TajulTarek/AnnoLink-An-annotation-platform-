package com.example.cmbss

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import com.example.cmbss.ui.theme.CmbssTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlin.random.Random

class AddPostActivity : AppCompatActivity() , AddPostCallBack {
    val addPostCallBack: AddPostCallBack =this
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val email= user?.email
    val id=user?.uid
    val ID=id.toString()
    val postsCollection = db.collection("users").document(ID).collection("myPost")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CmbssTheme {
                AddPostScreen(addPostCallBack)
            }
        }
    }

    override fun OnPost(title: String,deadline:String,description:String,salary:String,qualifications:String,time:String) {
        sentfirebase(title,deadline,description,salary,qualifications,time)
        val intent= Intent(this@AddPostActivity,studenthomeActivity::class.java)
        startActivity(intent)
    }

    fun sentfirebase(title: String,deadline:String,description:String,salary:String,qualifications:String,time:String){

        val allpostCollection = FirebaseFirestore.getInstance().collection("posts")
        val r= Random.nextInt()
        val EventId="$r$email$r"
        val postDocument = allpostCollection.document((EventId))
        val postData = hashMapOf(
            "email" to email,
            "title" to title,
            "deadline" to deadline,
            "description" to description,
            "salary" to salary,
            "qualification" to qualifications,
            "time" to time
        )
        val postID = hashMapOf(
            "postId" to EventId
        )

        /*usersCollection
            .add(postDocument)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }*/
        postDocument.set(postData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "User information stored successfully")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error storing user information", e)
            }

        val mypostDocument = db.collection("users").document(ID).collection("myPost").document(EventId)

        mypostDocument
            .set(postID)
            /*.addOnSuccessListener { documentReference ->
                // Post added successfully
                //val postId = documentReference.id
                // You can store the postId in the user's document or use it as needed
            }
            .addOnFailureListener { e ->
                // Handle errors
            }*/
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "User information stored successfully")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error storing user information", e)
            }

    }
}