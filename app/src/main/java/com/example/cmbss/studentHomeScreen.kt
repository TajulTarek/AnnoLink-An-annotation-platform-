package com.example.cmbss

import android.icu.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun studentHome (studentHomeCallBack: StudentHomeCallBack) {
    var isAvailable=false

    val dateFormat = SimpleDateFormat("HH:mm:ss dd:MM:yyyy", Locale.getDefault())
    val currentDateTime = dateFormat.format(Calendar.getInstance().time)
    val calendar=Calendar.getInstance().time

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
    /*for(i in 0..19){
        sentfirebase(i, Random.nextInt(200, 300))
    }*/
    val postsCollection = db.collection("posts")
    // Initialize an empty list to store job data
    var titles by remember {
        mutableStateOf<List<String>>(emptyList())
    }
    var descriptions by remember {
        mutableStateOf<List<String>>(emptyList())
    }
    var joblist by remember {
        mutableStateOf<List<JobPost>>(emptyList())
    }
    //joblist=getfromfire()
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
                        val timeFormat = SimpleDateFormat("HH:mm:ss")
                        var postedTime = dateFormat.parse(time)

                        try {
                            var postedTime = dateFormat.parse(time)
                            println(time)
                            val currentdateTime = dateFormat.parse(currentDateTime)
                            // Compare the datetimes
                            when {
                                postedTime.before(currentdateTime) -> {
                                    isAvailable=true
                                }
                                else -> {
                                    isAvailable=false
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        val justpostedTime = timeFormat.format(postedTime)


                        joblist+=JobPost(postId,jobtitle,jobdescription,poster_Email,justpostedTime,salary,qualification,deadline,isAvailable)
                        //println("xxxxxxxxx $postId")

                    }
                }
                // Now jobsList contains data from all documents in the "posts" collection
                // You can use this list as an array of jobs
                println("Jobs: $titles")
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }

    val user = Firebase.auth.currentUser
    user?.let {
        // Name, email address, and profile photo Url
        val name = it.displayName
        val email = it.email
        val photoUrl = it.photoUrl
        val emailVerified = it.isEmailVerified
        val uid = it.uid

        //NavDrawer(modifier = Modifier, titles = titles, descriptions = descriptions,studentHomeCallBack,joblist)
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
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .padding(start = 10.dp)
                                    .align(Alignment.CenterVertically)// Add some padding for a border effect
                            ) {
                                // Replace the placeholder image with the actual user profile photo
                                Image(
                                    painter = painterResource(id = R.drawable.person_icon),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop

                                )
                            }

                            /*IconButton(
                                onClick = { studentHomeCallBack.OnSignOut() },
                                modifier = Modifier.padding(start = 150.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Logout,
                                    contentDescription = "Logout",
                                    modifier=Modifier.size(60.dp)
                                )
                            }*/

                            //Text("$timeFormat")
                            IconButton(
                                onClick = { studentHomeCallBack.OnAddPost()},
                                modifier = Modifier.padding(start = 150.dp,top=10.dp)

                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Notifications,
                                    contentDescription = "Notifications",
                                    modifier=Modifier.size(30.dp),
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
                                    modifier=Modifier.size(30.dp),
                                    tint=Color.Black
                                )
                            }
                            IconButton(onClick = {
                                showMenu =! showMenu
                            },
                                modifier = Modifier.padding(top=10.dp)
                            ) {
                                Icon(imageVector = Icons.Filled.Menu,
                                    contentDescription = "menu Icon",
                                    tint=Color.Black)
                                DropdownMenu(modifier=Modifier.background(bgcolor),
                                    expanded = showMenu, onDismissRequest = { showMenu=false }) {
                                    DropdownMenuItem(text = { Text("Profile") }
                                        , onClick = { /*TODO*/ },
                                        leadingIcon = {
                                            Icon(imageVector = Icons.Filled.Person, contentDescription = "Profile Icon")
                                        }
                                    )
                                    Divider()
                                    DropdownMenuItem(text = { Text("My Posts") },
                                        onClick = { /*TODO*/ },
                                        leadingIcon = {
                                            Icon(imageVector = Icons.Filled.PostAdd, contentDescription = "")
                                        }
                                    )
                                    Divider()
                                    DropdownMenuItem(text = { Text("About Us") },
                                        onClick = { /*TODO*/ },
                                        leadingIcon = {
                                            Icon(imageVector = Icons.Filled.ReportGmailerrorred, contentDescription = "")
                                        }
                                    )
                                    Divider()
                                    DropdownMenuItem(text = { Text("Share") },
                                        onClick = {  },
                                        leadingIcon = {
                                            Icon(imageVector = Icons.Filled.Share, contentDescription = "")
                                        }
                                    )
                                    Divider()
                                    DropdownMenuItem(text = { Text("Logout") },
                                        onClick = { studentHomeCallBack.OnSignOut() },
                                        leadingIcon = {
                                            Icon(imageVector = Icons.Filled.Logout, contentDescription = "")
                                        }
                                    )

                                }//End drop menu

                            }



                        }
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp),
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
                            ))

                    }

                },

            ) { values ->

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(values)
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
    Card(
        modifier
            .padding(10.dp)
            .wrapContentSize()
            .clickable {
                studentHomeCallBack.OnPostClick(id)
            },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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
                        text = poster_Email,
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontFamily = FontFamily.Serif
                        ),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { }
                    )
                }

                // Time icon and time ago
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
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
@Composable
fun profile(){

}
