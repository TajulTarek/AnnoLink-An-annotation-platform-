package com.example.cmbss

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.cmbss.ui.theme.CmbssTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class AllApplicantsActivity : AppCompatActivity() ,AllApplicantsCallBack{
    private lateinit var postId:String
    private lateinit var receivedIntent: Intent
    val allApplicantsCallBack:AllApplicantsCallBack=this
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        receivedIntent=intent
        postId=(receivedIntent.getSerializableExtra("postId")as?String)!!

        super.onCreate(savedInstanceState)
        setContent {
            CmbssTheme {
                AllApplicantsScreen(allApplicantsCallBack,postId)
            }
        }
    }

    override fun OnOtherProfileClick(Id:String) {
        val intent= Intent(this@AllApplicantsActivity,MyProfileActivity::class.java)
        intent.putExtra("Id",Id)
        startActivity(intent)
    }

    override fun addOnChannel(userId: String,channelId:String) {
        val intent= Intent(this@AllApplicantsActivity,AllApplicantsActivity::class.java)
        intent.putExtra("postId",channelId)
        startActivity(intent)
        val channelsCollection = FirebaseFirestore.getInstance().collection("channels")
        val channelDocument = channelsCollection.document(channelId)
        channelDocument.update("members", FieldValue.arrayUnion(userId))
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                Log.e("TAG", "Error updating members array: $e")
            }
        val postsCollection = FirebaseFirestore.getInstance().collection("posts")
        val postDocument =postsCollection.document(channelId)
        postDocument.update("applicants", FieldValue.arrayRemove(userId))
        val mychannelDocument = db.collection("users").document(userId).collection("myChannel").document(channelId)
        val channelInfo = hashMapOf(
            "postId" to channelId,
            "isRated" to false
        )
        mychannelDocument
            .set(channelInfo)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
            }


    }
    override fun OnReject(userId: String,channelId:String) {
        val postsCollection = FirebaseFirestore.getInstance().collection("posts")
        val postDocument =postsCollection.document(channelId)
        postDocument.update("applicants", FieldValue.arrayRemove(userId))
        val intent= Intent(this@AllApplicantsActivity,AllApplicantsActivity::class.java)
        intent.putExtra("postId",channelId)
        startActivity(intent)
    }
}