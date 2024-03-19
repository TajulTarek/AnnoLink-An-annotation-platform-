package com.example.cmbss

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class chatPerson(
    val userId:String,
    val name:String
)
@Composable
fun ChatBox(chatBoxCallBack: ChatBoxCallBack){
    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentUserId = currentUser?.uid ?: ""
    var userInfo by remember {
        mutableStateOf<List<chatPerson>>(emptyList())
    }
    var user1 by remember { mutableStateOf("") }
    var user2 by remember { mutableStateOf("") }

    FirebaseFirestore.getInstance().collection("SingleChats")
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                val docId = document.id
                val userIds = docId.split("(##)")
                if (userIds.size == 2) {
                    println(userIds[0]+"  both   "+userIds[1])
                    if (userIds[0] == currentUserId || userIds[1]==currentUserId) {
                        if (userIds[0] == currentUserId) {
                            user2 = userIds[1]
                        } else {
                            user2 = userIds[0]
                        }
                        val otherUser=user2
                        val usersCollection = FirebaseFirestore.getInstance().collection("users")
                        usersCollection.document(user2)
                            .get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    val fullName = documentSnapshot.getString("fullname")
                                    if (fullName != null) {
                                        println("user2 $user2  fullname: $fullName")
                                        userInfo += chatPerson(otherUser, fullName)
                                    } else {
                                        println("Full name is null $fullName")
                                    }
                                } else {
                                    // Handle case where document does not exist
                                    println("Document does not exist")
                                }
                            }
                            .addOnFailureListener { exception ->
                                // Handle any errors
                                println("Error getting document: $exception")
                            }
                    }
                    else{

                    }

                }
            }
        }


    val bgcolor = Color(0xFF3EA7D7)
    val offwhite=Color(0xfffde4f2)
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bgcolor)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {

                Text(
                    modifier= Modifier,
                    text = "ChatBox",
                    color = Color.White,
                    fontSize = 24.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.White)
            )
            if(userInfo.size>0){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(offwhite)
                ) {
                    items(userInfo){it
                        eachChat(
                            chatBoxCallBack,it
                        )
                    }

                }

            }
            else{
                Box(
                    modifier = Modifier.fillMaxSize().background(offwhite),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No chat available",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

        }
    }
}

@Composable
fun eachChat(chatBoxCallBack: ChatBoxCallBack,chatPerson: chatPerson) {

    Card(
        modifier= Modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .wrapContentSize()
            .fillMaxWidth()
            .clickable {
                chatBoxCallBack.OnChatClick(chatPerson.userId)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(4.dp)
    ){
        Column(modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.Center
            )
        {
            Text(
                text = chatPerson.name,
                modifier = Modifier
                    .padding(start=10.dp),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                    fontSize = 18.sp
                )
            )
        }
    }
}