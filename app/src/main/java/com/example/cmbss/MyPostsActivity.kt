package com.example.cmbss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.cmbss.ui.theme.CmbssTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MyPostsActivity : AppCompatActivity(),MyPostsCallBack {
    val myPostsCallBack:MyPostsCallBack=this
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            CmbssTheme {


                MyPostScreen(myPostsCallBack)
            }
        }
    }

    override fun OnCardClick(postId:String) {
        val intent= Intent(this@MyPostsActivity,AllApplicantsActivity::class.java)
        intent.putExtra("postId",postId)
        startActivity(intent)
    }
}
/*
val postDocumentReference = FirebaseFirestore.getInstance().collection("posts").document(postId)
        postDocumentReference.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve the "applicants" array
                    val applicantsArray = documentSnapshot.get("applicants") as? List<String>
                    if (applicantsArray != null) {
                        // Use the applicantsArray as needed
                        for (applicantUid in applicantsArray) {

                        }
                    } else {
                        // "applicants" field is either null or not a List<String>
                    }
                } else {
                    // Document does not exist
                }
            }
            .addOnFailureListener { exception ->
                // Handle failures
                Log.e("Firestore", "Error getting post document", exception)
            }
 */
