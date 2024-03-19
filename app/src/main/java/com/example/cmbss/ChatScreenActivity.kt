package com.example.cmbss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent

class ChatScreenActivity : AppCompatActivity(),ChatScreenCallBack {
    private lateinit var channelId:String
    private lateinit var receivedIntent: Intent
    override fun onCreate(savedInstanceState: Bundle?) {
        val chatScreenCallBack:ChatScreenCallBack=this
        receivedIntent=intent
        channelId=(receivedIntent.getSerializableExtra("channelId")as?String)!!
        super.onCreate(savedInstanceState)
        setContent {
            ChatScreen(chatScreenCallBack,channelId)
        }
    }
    override fun OnOtherProfileClick(Id:String) {
        val intent= Intent(this@ChatScreenActivity,MyProfileActivity::class.java)
        intent.putExtra("Id",Id)
        startActivity(intent)
    }
}