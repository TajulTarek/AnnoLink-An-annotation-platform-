package com.example.cmbss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent

class PostDetailsActivity : AppCompatActivity() , PostDetailsCallBack {
    private lateinit var postId:String
    private lateinit var receivedIntent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        receivedIntent=intent
        postId=(receivedIntent.getSerializableExtra("postId")as?String)!!
        println("detailsxxxxx $postId")
        super.onCreate(savedInstanceState)
        setContent {
            PostDetails(postId)
        }
    }

    override fun OnBackArrow() {
        TODO("Not yet implemented")
    }

    override fun OnApply() {
        TODO("Not yet implemented")
    }
}