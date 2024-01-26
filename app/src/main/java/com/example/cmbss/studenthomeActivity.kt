package com.example.cmbss

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.cmbss.ui.theme.CmbssTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class studenthomeActivity : ComponentActivity() ,StudentHomeCallBack{
    val auth = FirebaseAuth.getInstance()
    val studentHomeCallBack:StudentHomeCallBack=this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CmbssTheme {
                    //HomeScreen()
                    studentHome(studentHomeCallBack)
                    //MyAccount()
                    //Myaccount(myAccountCallBack)

            }
        }
    }
    override fun OnSignOut(){
        auth.signOut()
        val intent= Intent(this@studenthomeActivity,MainActivity::class.java)
        startActivity(intent)
    }

    override fun OnPostClick(id:String) {
        val intent= Intent(this@studenthomeActivity,PostDetailsActivity::class.java)
        intent.putExtra("postId",id)
        startActivity(intent)
    }

    override fun OnAddPost() {
        val intent= Intent(this@studenthomeActivity,AddPostActivity::class.java)
        startActivity(intent)
    }

    /*fun sentfirebase(title:String,description: String){

// Reference to the Firestore database
        val db = Firebase.firestore

// Reference to the "posts" collection
        val postsCollection = db.collection("posts")

// Create a new job post
        val jobPost = JobPost(
            title = title,
            description = description
        )

// Add the job post to the "posts" collection
        postsCollection
            .document("job $") // Assuming "job" is the document ID
            .set(jobPost)
            .addOnSuccessListener {
                // Document added successfully
            }
            .addOnFailureListener { e ->
                // Error adding document
            }
    }*/
}

