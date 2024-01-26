package com.example.cmbss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.cmbss.ui.theme.CmbssTheme
import com.google.firebase.auth.FirebaseAuth

class MyAccountActivity : ComponentActivity() , MyAccountCallBack{
    val auth = FirebaseAuth.getInstance()
    val myAccountCallBack:MyAccountCallBack=this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CmbssTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyAccount(myAccountCallBack)
                    //ForgetPassword()
                }
            }
        }
    }

    override fun OnLogOut() {
        auth.signOut()
        val intent= Intent(this@MyAccountActivity,MainActivity::class.java)
        startActivity(intent)
    }
}