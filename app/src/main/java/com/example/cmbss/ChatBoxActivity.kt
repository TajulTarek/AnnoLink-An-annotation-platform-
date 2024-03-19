package com.example.cmbss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.google.firebase.auth.FirebaseAuth

class ChatBoxActivity : AppCompatActivity(),ChatBoxCallBack {
    val chatBoxCallBack:ChatBoxCallBack=this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChatBox(chatBoxCallBack)
        }
    }

    override fun OnChatClick(userId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val currentUserId = currentUser?.uid ?: ""
        val intent= Intent(this@ChatBoxActivity,SingleChatActivity::class.java)
        intent.putExtra("currentuserId",currentUserId)
        intent.putExtra("oppositeId",userId)
        startActivity(intent)
    }
}