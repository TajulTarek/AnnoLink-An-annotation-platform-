package com.example.cmbss

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
}