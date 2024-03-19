package com.example.cmbss

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleChat(currentUserId: String,oppositeId:String
) {
    var onScreenMsg by remember {
        mutableStateOf<List<String>>(emptyList())
    }
    var oppositeName by remember{ mutableStateOf("NaN") }

    var chatId by remember{ mutableStateOf("") }
    if(currentUserId<oppositeId){
        chatId=currentUserId+"(##)"+oppositeId
    }
    else{
        chatId=oppositeId+"(##)"+currentUserId
    }
    val firestore = FirebaseFirestore.getInstance()
    val chatDocument = firestore.collection("SingleChats").document(chatId)
    val oppositeDoc=firestore.collection("users").document(oppositeId)

    chatDocument.get()
        .addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                // Document doesn't exist, create it with an empty list of string arrays for messages
                val messages = emptyList<String>()
                val data = hashMapOf(
                    "chats" to messages
                )
                chatDocument.set(data)
                    .addOnSuccessListener {
                        println("Chat document created successfully")
                    }
                    .addOnFailureListener { exception ->
                        println("Error creating chat document: $exception")
                    }
            } else {
                println("Chat document already exists")
            }
        }
        .addOnFailureListener { exception ->
            println("Error getting chat document: $exception")
        }


    oppositeDoc.get()
        .addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val fullName = documentSnapshot.getString("fullname")
                if (fullName != null) {
                    // Use the full name here
                    oppositeName=fullName
                } else {
                    // Handle case where full name is null
                    println("Full name is null")
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


    DisposableEffect(key1 = chatId) {
        val listenerRegistration = chatDocument.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }
            snapshot?.let { docSnapshot ->
                val messages = docSnapshot.get("chats") as? List<String>
                onScreenMsg = messages.orEmpty()
            }
        }
        onDispose {
            listenerRegistration.remove()
        }
    }
    val bgcolor = Color(0xFF3EA7D7)
    val offwhite=Color(0xfffde4f2)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(bgcolor).height(60.dp),
            contentAlignment = Alignment.Center
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text =oppositeName,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color=Color.White,
                        fontFamily = FontFamily.Serif
                    ),
                    modifier = Modifier
                        .padding(start=20.dp,end=10.dp,top=10.dp)
                        .width(300.dp)
                        .heightIn(max = 100.dp, min = 40.dp)
                )

            }
        }

        // Chat Messages
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 16.dp)
        ) {
            LazyColumn(
                reverseLayout = true,
                modifier = Modifier.fillMaxSize()
            ) {
                items(onScreenMsg.reversed()) { message ->
                    if (currentUserId != null) {
                        ChatMessage(message = message,currentUserId)
                    }
                }
            }
        }

        // Input Field
        var messageText by remember { mutableStateOf("") }

        Row(verticalAlignment = Alignment.Bottom,
            modifier=Modifier.padding(vertical = 16.dp, horizontal = 10.dp)) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                placeholder = { Text(text = "Type a message...") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
                    .background(offwhite, RoundedCornerShape(10.dp))
            )

            Button(
                onClick = {
                    val firestore = FirebaseFirestore.getInstance()
                    val channelDocumentRef = firestore.collection("SingleChats").document(chatId)

                    channelDocumentRef.get()
                        .addOnSuccessListener { document ->
                            val currentMessages = document.get("chats") as? List<String> ?: emptyList()

                            val newMessage = currentUserId + "(-->" + messageText
                            val updatedMessages = currentMessages.toMutableList().apply { add(newMessage) }

                            channelDocumentRef.update("chats", updatedMessages)
                                .addOnSuccessListener {
                                    // Update local state if needed
                                    messageText = ""
                                }
                                .addOnFailureListener { e ->
                                    // Handle failure
                                }
                        }
                        .addOnFailureListener { e ->
                            // Handle failure
                        }
                }
            ) {
                Text(text = "Send")
            }
        }
    }
}
