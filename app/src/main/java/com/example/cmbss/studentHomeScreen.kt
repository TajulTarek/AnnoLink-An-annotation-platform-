package com.example.cmbss

import coil.compose.rememberImagePainter
import android.content.ContentValues.TAG
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.ReportGmailerrorred
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.tasks.await
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun studentHome (studentHomeCallBack: StudentHomeCallBack) {
    val offwhite= Color(0xfffde4f2)
    var isAvailable by remember { mutableStateOf(false) }
    val dateFormat = SimpleDateFormat("HH:mm:ss dd:MM:yyyy", Locale.getDefault())
    val deadlineFormat = SimpleDateFormat("dd:MM:yyyy", Locale.getDefault())
    val currentDateTime = dateFormat.format(Calendar.getInstance().time)
    val calendar=Calendar.getInstance().time
    var timeago by remember { mutableStateOf("") }
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    val searchHistory = listOf("Android", "Kotlin", "Compose",
        "Material Design", "GPT-4"
    )
    val bgcolor = Color(0xFF3EA7D7)
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val db= Firebase.firestore
    var showMenu by remember{
        mutableStateOf(false);
    }
    val contaxt = LocalContext.current

    val postsCollection = db.collection("posts")

    var titles by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    var descriptions by remember {
        mutableStateOf<List<String>>(emptyList())
    }

    var joblist by remember {
        mutableStateOf<List<JobPost>>(emptyList())
    }

    db.collection("posts")
            .get()
            .addOnSuccessListener { documents ->
                val temp=documents
                for (document in temp) {
                    // Access each document's data and add it to the list
                    if(document!=null){
                        val postId=document.id
                        val poster_Email=document.getString("email")?:""
                        val jobtitle = document.getString("title")?:""
                        val jobdescription=document.getString("description")?:""
                        val qualification=document.getString("qualification")?:""
                        val salary=document.getString("salary")?:""
                        val time=document.getString("time")?:""

                        val deadline=document.getString("deadline")?:""
                        val timeFormat = SimpleDateFormat("HH:mm")
                        var postedTime = dateFormat.parse(time)
                        var deadlineTimeFormat= deadlineFormat.parse(deadline)

                        try {
                            //var deadlinetimeFormat = dateFormat.parse(time)
                            //println(time)
                            //println(deadlinetimeFormat)
                            val currentdateTime = dateFormat.parse(currentDateTime)

                            timeago= getTimeAgo(postedTime,currentdateTime)
                            // Compare the datetimes
                            when {
                                deadlineTimeFormat.before(currentdateTime) -> {
                                    isAvailable=false
                                }
                                else -> {
                                    isAvailable=true
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        val justpostedTime = timeFormat.format(postedTime)


                        joblist+=JobPost(postId,jobtitle,jobdescription,poster_Email,timeago,salary,qualification,deadline,isAvailable)

                    }
                }
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }
    val userCollectionRef = db.collection("users")
    var profilePictureUrl by remember {
        mutableStateOf("")
    }
    val user =Firebase.auth.currentUser
    user?.let {

        val uid = it.uid
        val userDocumentRef = userCollectionRef.document(uid)
        userDocumentRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    profilePictureUrl = documentSnapshot.getString("dp") ?: ""
                } else {
                    // Document does not exist, handle accordingly
                }

            }
        val painter = rememberImagePainter(data = profilePictureUrl, builder = {
            crossfade(true)
            placeholder(R.drawable.person_icon) // Replace with a placeholder image resource
        })

        Surface(

        ) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize(),
                containerColor = Color.White,
                topBar = {
                    Column(){
                        Row(
                            modifier = Modifier
                                //.border(0.5.dp, Color.Gray)
                                .fillMaxWidth()
                                .background(bgcolor)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Image(
                                    painter = painter,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(MaterialTheme.shapes.medium)
                                )
                            }

                            IconButton(
                                onClick = { studentHomeCallBack.OnAddPost()},
                                modifier = Modifier.padding(start = 180.dp,top=10.dp)

                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Notifications,
                                    contentDescription = "Notifications",
                                    modifier=Modifier.size(25.dp),
                                    tint=Color.Black
                                )
                            }
                            IconButton(
                                onClick = { studentHomeCallBack.OnAddPost() },
                                modifier = Modifier.padding(top=10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = "Add Post",
                                    modifier=Modifier.size(25.dp),
                                    tint=Color.Black
                                )
                            }
                            IconButton(onClick = {
                                showMenu =! showMenu
                            },
                                modifier = Modifier.padding(top=10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "menu Icon",
                                    tint = Color.Black
                                )
                                DropdownMenu(modifier = Modifier.background(bgcolor),
                                    expanded = showMenu, onDismissRequest = { showMenu = false }) {
                                    DropdownMenuItem(text = { Text("Profile") },
                                        onClick = { studentHomeCallBack.OnMyProfile() },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Filled.Person,
                                                contentDescription = "Profile Icon"
                                            )
                                        }
                                    )
                                    Divider()
                                    DropdownMenuItem(text = { Text("My Posts") },
                                        onClick = { studentHomeCallBack.OnMyPosts() },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Filled.PostAdd,
                                                contentDescription = ""
                                            )
                                        }
                                    )
                                    Divider()
                                    DropdownMenuItem(text = { Text("About Us") },
                                        onClick = { /*TODO*/ },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Filled.ReportGmailerrorred,
                                                contentDescription = ""
                                            )
                                        }
                                    )
                                    Divider()
                                    DropdownMenuItem(text = { Text("Share") },
                                        onClick = { },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Filled.Share,
                                                contentDescription = ""
                                            )
                                        }
                                    )
                                    Divider()
                                    DropdownMenuItem(text = { Text("Logout") },
                                        onClick = { studentHomeCallBack.OnSignOut() },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Filled.Logout,
                                                contentDescription = ""
                                            )
                                        }
                                    )

                                }//End drop menu

                            }
                        }
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(0.5.dp)
                                .background(Color.Gray)
                        )
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .height(66.dp),
                            label = { Text("Search") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search Icon",
                                    tint = Color.Black
                                )
                            },
                            trailingIcon={
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "",
                                    tint=Color.Black
                                )
                                if(searchQuery.isNotEmpty()){
                                    Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "",
                                    tint=Color.Black
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Search,
                                keyboardType = KeyboardType.Text
                            ),
                            textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                            singleLine = true,
                            maxLines = 1,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Color.Black,
                                focusedLabelColor = Color.Black,
                                cursorColor = Color.Black,
                                textColor = Color.Black
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )

                    }

                },

            ) { values ->

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(values)
                        .background(Color.White)
                ) {

                    items(joblist) {
                        ColumnItem(studentHomeCallBack,
                            modifier=Modifier,
                            it.id,
                            it.title,
                            it.description,
                            it.poster_Email,
                            it.time,
                            it.salary,
                            it.qualification,
                            it.deadline,
                            it.isAvailable
                        )
                    }


                }

            }
        }
    }
}
@Composable
fun ColumnItem(studentHomeCallBack: StudentHomeCallBack,
                       modifier: Modifier,
                       id:String,
                       title: String,
                       descriptions: String,
                       poster_Email: String,
                       time:String,
                       salary:String,
                       qualification:String,
                       deadline:String,
                       isAvailable:Boolean
) {

    val offwhite=Color(0xffe5bdc4)
    val db = Firebase.firestore
    var userId=""
    var fullname by remember { mutableStateOf("") }
    val usersCollection = db.collection("users")
    usersCollection.whereEqualTo("email", poster_Email)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents) {
                userId = document.id
                fullname = document.getString("fullname").toString()
            }
        }
        .addOnFailureListener { exception ->
            // Handle any errors that occurred during the query
            Log.w(TAG, "Error getting documents: ", exception)
        }
    Card(

        modifier
            .padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 4.dp)
            .wrapContentSize()
            .clickable {
                studentHomeCallBack.OnPostClick(id,title,descriptions,deadline,salary,fullname,qualification)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(6.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Top Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Person icon and name
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = fullname,
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontFamily = FontFamily.Serif
                        ),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {

                            }
                    )
                }

                // Time icon and time ago
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = time,
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontStyle = FontStyle.Italic
                        )
                    )
                    val availabilityIcon = if (isAvailable) {
                        Icons.Default.CheckCircle
                    } else {
                        Icons.Default.Close
                    }

                    /*Icon(
                        imageVector = availabilityIcon,
                        contentDescription = null,
                        tint = if (true) Color.Green else Color.Red,
                        modifier = Modifier.size(24.dp)
                    )*/

                }
            }

            // Job Title
            Text(
                text = title,
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    fontFamily = FontFamily.SansSerif
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
            )

            // Availability Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val availabilityIcon = if (isAvailable) {
                    Icons.Default.CheckCircle
                } else {
                    Icons.Default.Close
                }


                // Spacer
                Spacer(modifier = Modifier.width(4.dp))

                // Availability Text
                Text(
                    text = if (isAvailable) "Available" else "Not Available",
                    color = if (isAvailable) Color.Green else Color.Red
                )
            }
        }
    }

}


data class DrawerItems(
    val icon : ImageVector,
    val text : String,
    val badgeCount : Int,
    val hasBadge : Boolean
)

data class JobPost(
    val id:String,
    val title: String,
    val description: String,
    val poster_Email: String,
    val time:String,
    val salary:String,
    val qualification:String,
    val deadline:String,
    val isAvailable: Boolean
)
fun getTimeAgo(postedTime: Date, presentTime: Date): String {
    val differenceInMillis = presentTime.time - postedTime.time
    val differenceInSeconds = differenceInMillis / 1000
    val differenceInMinutes = differenceInSeconds / 60
    val differenceInHours = differenceInMinutes / 60
    val differenceInDays = differenceInHours / 24
    val differenceInMonths = differenceInDays / 30

    return when {
        differenceInMonths > 1 -> "${differenceInMonths.toInt()} months ago"
        differenceInDays > 1 -> "${differenceInDays.toInt()} days ago"
        differenceInHours > 1 -> "${differenceInHours.toInt()} hours ago"
        differenceInMinutes > 1 -> "${differenceInMinutes.toInt()} minutes ago"
        else -> "just now"
    }
}


@Composable
fun profile(){

}
