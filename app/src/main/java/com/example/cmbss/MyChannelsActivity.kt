package com.example.cmbss

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.cmbss.ui.theme.CmbssTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat

class MyChannelsActivity : AppCompatActivity(),MyChannelsCallBack {
    val myChannelsCallBack:MyChannelsCallBack =this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val currentUser = FirebaseAuth.getInstance().currentUser
            val uid = currentUser?.uid
            var myChannels by remember {
                mutableStateOf<List<studenthomeActivity.Channel>>(emptyList())
            }
            if (uid != null) {
                val mychannelCollection = FirebaseFirestore.getInstance().collection("users").document(uid).collection("myChannel")

                LaunchedEffect(uid) {
                    try {
                        // Fetch postID documents
                        val channelIDDocuments = mychannelCollection.get().await()

                        // Iterate over postIDDocuments and fetch posts
                        for (document in channelIDDocuments) {
                            val channelId = document.getString("postId")
                            if (channelId != null) {
                                val channelDocument = FirebaseFirestore.getInstance().collection("channels").document(channelId).get().await()
                                val title=channelDocument.getString("title")?:""
                                val owner=channelDocument.getString("channelOwner")?:""
                                val membersArray = channelDocument.get("members") as? List<String>
                                val size= membersArray?.size
                                myChannels+= studenthomeActivity.Channel(
                                    channelId,
                                    owner,
                                    emptyList(),
                                    membersArray,
                                    title
                                )

                            }

                        }
                    } catch (e: Exception) {

                        e.printStackTrace()
                    }
                }

            }
                MyChannelsScreen(myChannelsCallBack,myChannels)
        }
    }

    override fun onChannelClick(channelId: String) {
        val intent= Intent(this@MyChannelsActivity,ChatScreenActivity::class.java)
        intent.putExtra("channelId",channelId)
        startActivity(intent)
    }
}