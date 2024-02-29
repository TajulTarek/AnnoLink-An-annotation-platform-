package com.example.cmbss

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import com.example.cmbss.ui.theme.CmbssTheme

class MyProfileActivity : AppCompatActivity(),MyProfileCallBack {
    private lateinit var Id:String
    private lateinit var receivedIntent: Intent
    val myProfileCallBack:MyProfileCallBack=this

    override fun onCreate(savedInstanceState: Bundle?) {
        receivedIntent=intent
        Id=(receivedIntent.getSerializableExtra("Id")as?String)!!
        super.onCreate(savedInstanceState)
        setContent {
            CmbssTheme {
                MyProfile(myProfileCallBack,Id)
            }
        }
    }

    override fun OnGithub(url:String) {
        val url = url // Replace with your desired URL
        println(url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Handle the case where no activity can handle the intent (e.g., no browser installed)
            Log.e("MyProfile", "No browser app found to open the link.", e)
            // You can display a user-friendly message here
        }
    }


    override fun OnLinkedin(url:String) {
        val url = url// Replace with your desired URL
        println(url)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Handle the case where no activity can handle the intent (e.g., no browser installed)
            Log.e("MyProfile", "No browser app found to open the link.", e)
            // You can display a user-friendly message here
        }
    }
}