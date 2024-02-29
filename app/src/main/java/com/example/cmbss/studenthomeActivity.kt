package com.example.cmbss

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.cmbss.ui.theme.CmbssTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class studenthomeActivity : ComponentActivity() ,StudentHomeCallBack{
    val auth = FirebaseAuth.getInstance()
    val studentHomeCallBack:StudentHomeCallBack=this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CmbssTheme {
                    studentHome(studentHomeCallBack)

            }
        }
    }
    override fun OnSignOut(){
        auth.signOut()
        val intent= Intent(this@studenthomeActivity,MainActivity::class.java)
        startActivity(intent)
    }

    override fun OnPostClick(id:String,title:String,description:String,deadline:String,salary:String,fullname:String,qualification:String) {
        val intent= Intent(this@studenthomeActivity,PostDetailsActivity::class.java)
        intent.putExtra("postId",id)
        intent.putExtra("title",title)
        intent.putExtra("description",description)
        intent.putExtra("deadline",deadline)
        intent.putExtra("salary",salary)
        intent.putExtra("fullname",fullname)
        intent.putExtra("qualification",qualification)

        startActivity(intent)
    }

    override fun OnAddPost() {
        val intent= Intent(this@studenthomeActivity,AddPostActivity::class.java)
        startActivity(intent)
    }
    override fun OnMyPosts(){
        val intent= Intent(this@studenthomeActivity,MyPostsActivity::class.java)
        startActivity(intent)
    }

    override fun OnMyProfile() {
        val user=auth.currentUser
        val Id=user?.uid
        val intent= Intent(this@studenthomeActivity,MyProfileActivity::class.java)
        intent.putExtra("Id",Id)
        startActivity(intent)
    }
}

