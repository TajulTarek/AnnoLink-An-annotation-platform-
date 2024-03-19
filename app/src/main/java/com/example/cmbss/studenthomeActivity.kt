package com.example.cmbss

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlin.random.Random

class studenthomeActivity : ComponentActivity() ,StudentHomeCallBack{
    val auth = FirebaseAuth.getInstance()
    val studentHomeCallBack:StudentHomeCallBack=this
    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser
    val email= user?.email
    val id=user?.uid
    val ID=id.toString()

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

    override fun OnPostClick(id:String,title:String,description:String,deadline:String,salary:String,fullname:String,qualification:String,
                             isAvailable:Boolean) {
        val intent= Intent(this@studenthomeActivity,PostDetailsActivity::class.java)
        intent.putExtra("postId",id)
        intent.putExtra("title",title)
        intent.putExtra("description",description)
        intent.putExtra("deadline",deadline)
        intent.putExtra("salary",salary)
        intent.putExtra("fullname",fullname)
        intent.putExtra("qualification",qualification)
        intent.putExtra("isAvailable",isAvailable)
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
    override fun OnPost(title: String,deadline:String,description:String,salary:String,qualifications:String,time:String) {
        sentfirebase(title,deadline,description,salary,qualifications,time)
    }

    override fun OnMychannels() {
        val intent= Intent(this@studenthomeActivity,MyChannelsActivity::class.java)
        startActivity(intent)
    }

    override fun OnOtherProfile(userId: String) {
        val intent= Intent(this@studenthomeActivity,MyProfileActivity::class.java)
        intent.putExtra("Id",userId)
        startActivity(intent)
    }

    override fun OnChatBox() {
        val intent= Intent(this@studenthomeActivity,ChatBoxActivity::class.java)
        startActivity(intent)
    }

    data class Channel(
        val channelId: String,
        val channelOwner: String,
        val chats: List<Chat>,
        val members: List<String>?,
        val title:String
    )
    data class Chat(
        val userId: String,
        val message: String,
        val timestamp: Long
    )
    fun sentfirebase(title: String,deadline:String,description:String,salary:String,qualifications:String,time:String){
        val channelsCollection = FirebaseFirestore.getInstance().collection("channels")

        val allpostCollection = FirebaseFirestore.getInstance().collection("posts")
        val r= Random.nextInt()
        val EventId="$r$email$r"
        val channelDocument = channelsCollection.document((EventId))
        val postDocument = allpostCollection.document((EventId))
        val initialMembers = listOf(ID)
        val channelData = Channel(
            channelId = EventId,
            channelOwner=ID,
            chats = emptyList(),
            members = initialMembers,
            title=title
        )

        val postData = hashMapOf(
            "email" to email,
            "title" to title,
            "deadline" to deadline,
            "description" to description,
            "salary" to salary,
            "qualification" to qualifications,
            "time" to time,
            "applicants" to emptyList<String>()
        )
        val postID = hashMapOf(
            "postId" to EventId
        )
        val channelInfo = hashMapOf(
            "postId" to EventId,
            "isRated" to true
        )
        channelDocument.set(channelData)
            .addOnSuccessListener {
                // Channel data saved successfully, you can proceed with other operations if needed
            }
            .addOnFailureListener { e ->
                // Handle any errors
                Log.e(TAG, "Error creating channel", e)
            }

        postDocument.set(postData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "User information stored successfully")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error storing user information", e)
            }

        val mypostDocument = db.collection("users").document(ID).collection("myPost").document(EventId)
        val mychannelDocument = db.collection("users").document(ID).collection("myChannel").document(EventId)
        mychannelDocument
            .set(channelInfo)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "User information stored successfully")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error storing user information", e)
            }
        mypostDocument
            .set(postID)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "User information stored successfully")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error storing user information", e)
            }

    }
}

