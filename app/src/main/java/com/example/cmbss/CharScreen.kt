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
fun ChatScreen(chatScreenCallBack: ChatScreenCallBack,
    channelId: String
) {
    println(channelId)

    var isDialogVisible by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val uid = currentUser?.uid
    var title by remember { mutableStateOf("") }
    var ownerId by remember { mutableStateOf("") }
    var onScreenMsg by remember {
        mutableStateOf<List<String>>(emptyList())
    }
    val firestore = FirebaseFirestore.getInstance()
    val channelDocumentRef = firestore.collection("channels").document(channelId)
    DisposableEffect(key1 = channelId) {
        val listenerRegistration = channelDocumentRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }
            snapshot?.let { docSnapshot ->
                val messages = docSnapshot.get("chats") as? List<String>
                title=docSnapshot.get("title") as String
                ownerId=docSnapshot.get("channelOwner") as String
                onScreenMsg = messages.orEmpty()
            }
        }
        onDispose {
            listenerRegistration.remove()
        }
    }
    val bgcolor = Color(0xFF3EA7D7)
    val offwhite=Color(0xfffde4f2)
    val topbarcolor=Color(0xffead0d0)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Title
        Box(
            Modifier
                .fillMaxWidth()
                .background(bgcolor),
            contentAlignment = Alignment.Center
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text =title,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color=Color.White,
                        fontFamily = FontFamily.Serif
                    ),
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .width(300.dp)
                        .heightIn(max = 100.dp, min = 40.dp)
                )
                IconButton(onClick = { isDialogVisible=true; }) {
                    Icon(imageVector = Icons.Filled.People, contentDescription = "",tint=Color.White)
                }
                if (isDialogVisible) {
                    allMembers(
                        chatScreenCallBack, onDismiss = { isDialogVisible = false },channelId, ownerId
                    )
                }
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
                    if (uid != null) {
                        ChatMessage(message = message,uid)
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
                    val channelDocumentRef = firestore.collection("channels").document(channelId)
                    //val newMessage = uid+"(-->"+messageText

                    // Update Firestore document with new message
                    channelDocumentRef.get()
                        .addOnSuccessListener { document ->
                            val currentMessages = document.get("chats") as? List<String> ?: emptyList()

                            val newMessage = uid + "(-->" + messageText
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

@Composable
fun ChatMessage(message: String, currentUserId: String) {

    val bgcolor = Color(0xFF3EA7D7)
    var senderName by remember { mutableStateOf("") }
    var messageText by remember { mutableStateOf("") }
    var senderUid by remember { mutableStateOf("") }
    val parts = message.split("(-->")
    if (parts.size == 2) {
        senderUid = parts[0]
        messageText = parts[1]
    }

    val isCurrentUser = senderUid == currentUserId

    val userDocumentReference = FirebaseFirestore.getInstance().collection("users").document(senderUid)
    userDocumentReference.get()
        .addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                val fullName = documentSnapshot.getString("fullname")
                if (fullName != null) {
                    senderName=fullName
                } else {
                    println("Full Name not found")
                }
            } else {
                println("User document not found")
            }
        }
        .addOnFailureListener { exception ->
            println("Error getting user document: $exception")
        }
    Box(modifier=Modifier.fillMaxWidth(),
        ){
        Column(modifier = Modifier
            .padding(start = 12.dp, end = 12.dp)
            .fillMaxWidth(),
            horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
        ) {
            Text(
                text = senderName,
                style = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Gray
                ),
                modifier = Modifier.padding(bottom = 3.dp)
            )
            Text(
                text = messageText,
                style = TextStyle(
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .background(if(isCurrentUser)bgcolor else Color.LightGray, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            )
        }
    }
}
data class member(
    val userId:String,
    val name:String,
    val isRated: Boolean
)

@Composable
fun allMembers(chatScreenCallBack: ChatScreenCallBack,
    onDismiss: () -> Unit,channelId: String,ownerId: String
) {
    var memberNames by remember { mutableStateOf<List<String>>(emptyList()) }
    var memberInfo by remember { mutableStateOf<List<member>>(emptyList()) }
    var postContent by remember { mutableStateOf("") }
    var memberIds by remember { mutableStateOf<List<String>>(emptyList()) }

    val firestore = FirebaseFirestore.getInstance()
    val channelDocumentRef = firestore.collection("channels").document(channelId)
    DisposableEffect(key1 = channelId) {
        val listenerRegistration = channelDocumentRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            snapshot?.let { docSnapshot ->
                val userIds = docSnapshot.get("members") as? List<String>
                if (userIds != null) {
                    userIds.forEach { userId ->
                        val userDocumentRef = firestore.collection("users").document(userId)
                        userDocumentRef.get()
                            .addOnSuccessListener { documentSnapshot ->
                                if (documentSnapshot.exists()) {
                                    val fullName = documentSnapshot.getString("fullname")
                                    if (fullName != null) {
                                        memberNames+=(fullName)

                                        userDocumentRef.collection("myChannel")
                                            .document(channelId)
                                            .get()
                                            .addOnSuccessListener { documentSnapshot ->
                                                if (documentSnapshot != null && documentSnapshot.exists()) {
                                                    val isRated = documentSnapshot.getBoolean("isRated")
                                                    // Use the value of isRated here
                                                    if(isRated!=null)
                                                    {
                                                        memberInfo += member(userId,fullName, isRated)
                                                    }
                                                } else {
                                                    // Document does not exist or is null
                                                }
                                            }
                                            .addOnFailureListener { exception ->
                                                // Handle any errors
                                                println("Error getting document: $exception")
                                            }

                                    } else {
                                        println("Full Name not found for user: $userId")
                                    }
                                } else {
                                    println("User document not found for user: $userId")
                                }
                            }
                            .addOnFailureListener { exception ->
                                println("Error getting user document: $exception")
                            }
                    }
                }
            }
        }
        onDispose {
            listenerRegistration.remove()
        }
    }
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnClickOutside = true)
    ) {
        sidebar(chatScreenCallBack,onDismiss,memberNames,memberInfo,channelId, ownerId)
    }
}

fun fetchMemberNames(userIds: List<String>) {
    val firestore = FirebaseFirestore.getInstance()
}

@Composable
fun sidebar(chatScreenCallBack: ChatScreenCallBack,onDismiss: () -> Unit,
            memberNamesList:List<String>,memberInfo:List<member>,channelId: String,
            ownerId:String)
{

    println(memberNamesList.size)

    val bgcolor = Color(0xFF3EA7D7)
    Box(modifier = Modifier
        .fillMaxHeight()
        .padding(20.dp)){
        Box(
            modifier = Modifier
                .background(bgcolor, RoundedCornerShape(5.dp))
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = "Members",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Serif
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Divider(color = Color.White, thickness = 1.dp, modifier = Modifier.fillMaxWidth())

                LazyColumn(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    items(memberInfo){ memberInfo ->
                        eachMember(
                            chatScreenCallBack,memberInfo,channelId, ownerId
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun eachMember(chatScreenCallBack: ChatScreenCallBack,member: member,channelId: String,ownerId:String){
    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentuserId = currentUser?.uid
    var ratedDialoge by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        val underlinedText = buildAnnotatedString {
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append(member.name)
            }
        }
        ClickableText(
            text = underlinedText,
            onClick = {
                    chatScreenCallBack.OnOtherProfileClick(member.userId)
            },
            modifier = Modifier.padding(vertical = 4.dp)
        )
        if(!member.isRated && currentuserId==ownerId){
            Box(
                modifier = Modifier
                    .clickable {
                        ratedDialoge=true
                    }
                    .padding(start = 8.dp)
                    .width(60.dp)
                    .height(30.dp)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Rate", color = Color.White, fontSize = 14.sp)
            }
            if (ratedDialoge) {
                var givenRating by remember { mutableStateOf(0.0) }
                Dialog(onDismissRequest = { ratedDialoge=false },
                    properties = DialogProperties(dismissOnClickOutside = true)
                ) {
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .wrapContentSize(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(10.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Rate The Member",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                for (index in 1..5) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(if (index <= givenRating) Color(0xFF00FFFF) else Color.White)
                                            .clickable {
                                                givenRating = index.toDouble()
                                            }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = "Star",
                                            tint = Color.Green,
                                            modifier = Modifier
                                                .size(24.dp)
                                                .align(Alignment.Center)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    ratedDialoge = false
                                    rated_The_member(member.userId,givenRating,channelId)
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF008B8B))
                            ) {
                                Text(
                                    text = "Submit",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun rated_The_member(userId: String, givenRating: Double, channelId: String){
    val firestore=FirebaseFirestore.getInstance()
    val userDocument=firestore.collection("users").document(userId)
    userDocument.update(
        mapOf(
            "total_annotate" to FieldValue.increment(1),
            "all_Rating" to FieldValue.increment(givenRating)
        )
    ).addOnSuccessListener {
        // Successfully updated user's rating information
    }.addOnFailureListener { e ->
        // Failed to update user's rating information
    }
    val myChannelDocumentRef = userDocument.collection("myChannel").document(channelId)
    myChannelDocumentRef.update("isRated", true)
        .addOnSuccessListener {
            // Successfully marked the channel as rated
        }.addOnFailureListener { e ->
            // Failed to mark the channel as rated
        }
}
