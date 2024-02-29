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

                var isAvailable by remember { mutableStateOf(false) }
                var timeago by remember { mutableStateOf("") }
                val dateFormat = SimpleDateFormat("HH:mm:ss dd:MM:yyyy", Locale.getDefault())
                val deadlineFormat = SimpleDateFormat("dd:MM:yyyy", Locale.getDefault())
                val currentDateTime = dateFormat.format(Calendar.getInstance().time)
                var myPosts by remember {
                    mutableStateOf<List<Post>>(emptyList())
                }
                val currentUser = FirebaseAuth.getInstance().currentUser
                val uid = currentUser?.uid
                if (uid != null) {
                    val myPostCollection = FirebaseFirestore.getInstance().collection("users").document(uid).collection("myPost")

                    LaunchedEffect(uid) {
                        try {
                            // Fetch postID documents
                            val postIDDocuments = myPostCollection.get().await()

                            // Iterate over postIDDocuments and fetch posts
                            for (document in postIDDocuments) {
                                val postId = document.getString("postId")
                                if (postId != null) {
                                    val postDocument = FirebaseFirestore.getInstance().collection("posts").document(postId).get().await()
                                    val title=postDocument.getString("title")?:""
                                    val time=postDocument.getString("time")?:""
                                    val deadline=postDocument.getString("deadline")?:""
                                    val timeFormat = SimpleDateFormat("HH:mm")
                                    var postedTime = dateFormat.parse(time)
                                    var deadlineTimeFormat= deadlineFormat.parse(deadline)
                                    val applicantsArray = postDocument.get("applicants") as? List<String>
                                    val size= applicantsArray?.size
                                    try {

                                        val currentdateTime = dateFormat.parse(currentDateTime)
                                        println(currentdateTime)
                                        timeago= getTimeAgo(postedTime,currentdateTime)
                                        when {
                                            deadlineTimeFormat.before(currentdateTime) -> {
                                                isAvailable=false
                                            }
                                            else -> {
                                                isAvailable=true
                                            }
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    myPosts+=Post(postId,title,timeago,isAvailable, size?:0 )
                                }

                            }
                        } catch (e: Exception) {

                            e.printStackTrace()
                        }
                    }

                }
                MyPostScreen(myPostsCallBack,myPosts)
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
