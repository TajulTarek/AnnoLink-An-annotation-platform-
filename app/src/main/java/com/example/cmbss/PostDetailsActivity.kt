package com.example.cmbss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlin.properties.Delegates

class PostDetailsActivity : AppCompatActivity() , PostDetailsCallBack {
    private lateinit var postId:String
    private lateinit var title:String
    private lateinit var description:String
    private lateinit var deadline:String
    private lateinit var salary:String
    private lateinit var fullname:String
    private lateinit var qualification:String
    private var isAvailable by Delegates.notNull<Boolean>()

    private lateinit var receivedIntent: Intent

    val postDetailsCallBack:PostDetailsCallBack=this
    val db = Firebase.firestore
    val postsCollection=db.collection("posts")
    override fun onCreate(savedInstanceState: Bundle?) {
        receivedIntent=intent
        postId=(receivedIntent.getSerializableExtra("postId")as?String)!!
        title=(receivedIntent.getSerializableExtra("title")as?String)!!
        description=(receivedIntent.getSerializableExtra("description")as?String)!!
        deadline=(receivedIntent.getSerializableExtra("deadline")as?String)!!
        salary=(receivedIntent.getSerializableExtra("salary")as?String)!!
        fullname=(receivedIntent.getSerializableExtra("fullname")as?String)!!
        qualification=(receivedIntent.getSerializableExtra("qualification")as?String)!!
        isAvailable=(receivedIntent.getSerializableExtra("isAvailable")as?Boolean)!!
        super.onCreate(savedInstanceState)
        setContent {
            PostDetails(postDetailsCallBack,postId,title,description,deadline,salary,fullname,qualification)
        }
    }

    override fun OnBackArrow() {
        TODO("Not yet implemented")
    }

    override fun OnApply(posterId:String,isAlreadyApplied:Boolean) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentuserId = currentUser?.uid
        if(currentuserId==posterId){
            Toast.makeText(
                baseContext,
                "You can not apply in own post.",
                Toast.LENGTH_SHORT,
            ).show()
        }
        else if(isAlreadyApplied){
            Toast.makeText(
                baseContext,
                "You already applied in this post ",
                Toast.LENGTH_SHORT,
            ).show()
        }
        else if(isAvailable) {
            val postDocumentRef = postsCollection.document(postId)
            val auth = FirebaseAuth.getInstance()
            val currentUser = Firebase.auth.currentUser
            if (currentUser != null) {
                val id = currentUser.uid
                postDocumentRef.update("applicants", FieldValue.arrayUnion(id))
                    .addOnSuccessListener {
                        // Value added successfully
                        val intent = Intent(this@PostDetailsActivity, studenthomeActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        // Handle the error
                    }
            }
        }
        else{
            Toast.makeText(
                baseContext,
                "Deadline is over! ",
                Toast.LENGTH_SHORT,
            ).show()
        }
    }
}