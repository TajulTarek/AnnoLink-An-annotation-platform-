package com.example.cmbss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent

class SingleChatActivity : AppCompatActivity() {
    private lateinit var currentuserId:String
    private lateinit var oppositeId:String

    private lateinit var receivedIntent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        receivedIntent=intent
        currentuserId=(receivedIntent.getSerializableExtra("currentuserId")as?String)!!
        oppositeId=(receivedIntent.getSerializableExtra("oppositeId")as?String)!!
        super.onCreate(savedInstanceState)
        setContent {
            SingleChat(currentuserId, oppositeId)
        }
    }
}