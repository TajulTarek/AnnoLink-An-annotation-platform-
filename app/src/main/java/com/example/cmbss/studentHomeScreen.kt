package com.example.cmbss

import android.app.DatePickerDialog
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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.ReportGmailerrorred
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
    var isDialogVisible by remember { mutableStateOf(false) }
    var postContent by remember { mutableStateOf("") }
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
    var filteredJobList by remember { mutableStateOf<List<JobPost>>(emptyList()) }

    var isFetched by rememberSaveable { mutableStateOf(false) }




    db.collection("posts")
            .get()
            .addOnSuccessListener { documents ->
                val temp=documents
                joblist= emptyList()
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
                isFetched=true
            }
            .addOnFailureListener { exception ->
                println("Error getting documents: $exception")
            }





    if (searchQuery.isNotEmpty()) {
        filteredJobList = joblist.filter {
            it.title.contains(searchQuery, ignoreCase = true) ||
                    it.description.contains(searchQuery, ignoreCase = true)
        }
    } else {
        filteredJobList = joblist
    }



    val userCollectionRef = db.collection("users")
    var profilePictureUrl by remember {
        mutableStateOf("")
    }
    val user =Firebase.auth.currentUser
    user?.let {
        if (!isFetched) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                    RoundedLoadingIndicator()
            }
        } else {


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
                        Column() {
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
                                        .clickable { studentHomeCallBack.OnMyProfile() }
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
                                    onClick = { studentHomeCallBack.OnChatBox() },
                                    modifier = Modifier.padding(start = 180.dp, top = 10.dp)

                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Chat,
                                        contentDescription = "Chat",
                                        modifier = Modifier.size(25.dp),
                                        tint = Color.White
                                    )
                                }
                                IconButton(
                                    onClick = { isDialogVisible = true; },
                                    modifier = Modifier.padding(top = 10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "Add Post",
                                        modifier = Modifier.size(25.dp),
                                        tint = Color.White
                                    )
                                }
                                if (isDialogVisible) {
                                    CreatePostDialog(
                                        onDismiss = { isDialogVisible = false },
                                        studentHomeCallBack
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        showMenu = !showMenu
                                    },
                                    modifier = Modifier.padding(top = 10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "menu Icon",
                                        tint = Color.White
                                    )
                                    DropdownMenu(modifier = Modifier.background(bgcolor),
                                        expanded = showMenu,
                                        onDismissRequest = { showMenu = false }) {
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
                                        DropdownMenuItem(text = { Text("My Channels") },
                                            onClick = { studentHomeCallBack.OnMychannels() },
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Filled.Groups,
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
                                    .height(66.dp)
                                    .padding(start = 10.dp, end = 10.dp),
                                label = { Text("Search") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = "Search Icon",
                                        tint = Color.Black
                                    )
                                },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Mic,
                                        contentDescription = "",
                                        tint = Color.Black
                                    )
                                    if (searchQuery.isNotEmpty()) {
                                        Icon(
                                            imageVector = Icons.Default.Mic,
                                            contentDescription = "",
                                            tint = Color.Black
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
                        items(filteredJobList) {
                            ColumnItem(
                                studentHomeCallBack,
                                modifier = Modifier,
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
}
data class Chat(
    val userId: String,
    val message: String,
    val timestamp: Long
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostDialog(
    onDismiss: () -> Unit,studentHomeCallBack: StudentHomeCallBack
) {
    var postContent by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        AddPostScreen1(onDismiss,studentHomeCallBack)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen1(onDismiss: () -> Unit,studentHomeCallBack: StudentHomeCallBack) {
    val calendar= java.util.Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("HH:mm:ss dd:MM:yyyy", Locale.getDefault())
    //var tim= DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar)
    var datetime="datetime"
    val c= java.util.Calendar.getInstance()
    val year=c.get(java.util.Calendar.YEAR)
    val month=c.get(java.util.Calendar.MONTH)
    val day=c.get(java.util.Calendar.DAY_OF_MONTH)
    val context= LocalContext.current
    var deadline by remember{
        mutableStateOf("  dd - mm - yy")
    }


    var deadlineText by remember { mutableStateOf("  Select Deadline ") }
    val datepicker= DatePickerDialog(
        context,{ d,year1,month1,day1->
            val month=month1+1
            val finalday=String.format("%02d",day1)
            val finalmonth=String.format("%02d",month)
            deadline="$finalday:$finalmonth:$year1"
            deadlineText= "  Deadline : $deadline"
        },year,month,day
    )

    var title by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var qualifications by remember { mutableStateOf("") }
    val bgcolor = Color(0xFF3EA7D7)
    Box(
        modifier = Modifier.background(bgcolor, RoundedCornerShape(20.dp))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(30.dp)){
            Text(
                text = "Add Post",
                modifier = Modifier
                    .fillMaxWidth(),
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal,
                    letterSpacing = 1.sp
                ),
                color = Color.White,//colorResource(id = R.color.black),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.padding(5.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(text = "Title") },
                placeholder = { Text(text = "title") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                value = title,
                onValueChange = {
                    title = it
                },
                leadingIcon={
                    Icon(
                        imageVector = Icons.Filled.Title,
                        contentDescription = "",
                        tint = Color.White
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White, // Set the text color to black
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Black
                )

            )

            Spacer(modifier = Modifier.padding(5.dp))

            Box(modifier= Modifier
                .fillMaxWidth()
                .border(
                    width = 0.5.dp, // Border width in dp
                    color = Color.Black // Border shape (you can customize it)
                )
                .height(50.dp)
                .clickable {
                    datepicker.show()
                }){
                Text(modifier=Modifier.padding(top=10.dp),text=deadlineText,color=Color.White)
            }

            Spacer(modifier = Modifier.padding(5.dp))

            OutlinedTextField(
                label = { Text(text = "Description") },
                placeholder = { Text(text = "Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                value = description,
                onValueChange = {
                    description = it
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White, // Set the text color to black
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.padding(5.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(text = "Salary") },
                placeholder = { Text(text = "Salary per hour") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                value = salary,
                onValueChange = {
                    salary= it
                },
                leadingIcon={
                    Icon(
                        imageVector = Icons.Filled.MonetizationOn,
                        contentDescription = "",
                        tint = Color.Black
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White, // Set the text color to black
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Black
                )

            )

            Spacer(modifier = Modifier.padding(5.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                label = { Text(text = "Desired qualification") },
                placeholder = { Text(text = "qualification") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                value = qualifications,
                onValueChange = {
                    qualifications = it
                },

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.White, // Set the text color to black
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = Color.Black
                )

            )


            Spacer(modifier = Modifier.padding(10.dp))

            Button(onClick = {
                val postedTime = dateFormat.format(android.icu.util.Calendar.getInstance().time)
                studentHomeCallBack.OnPost(title,deadline,description,salary,qualifications,postedTime)
                onDismiss()
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp)) {
                Text(text = "Post",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        fontStyle = FontStyle.Normal,
                        letterSpacing = 1.sp,
                    ))
            }

            Spacer(modifier = Modifier.padding(10.dp))


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
    val bgcolor = Color(0xFF3EA7D7)
    val db = Firebase.firestore
    var userId by remember { mutableStateOf("") }
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
                studentHomeCallBack.OnPostClick(
                    id,
                    title,
                    descriptions,
                    deadline,
                    salary,
                    fullname,
                    qualification,
                    isAvailable
                )
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
                            color = bgcolor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontFamily = FontFamily.Serif
                        ),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {
                                studentHomeCallBack.OnOtherProfile(userId)
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
fun RoundedLoadingIndicator(modifier: Modifier = Modifier) {
    Surface(
        color = Color.White,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(16.dp)
                .size(48.dp)
        ) {
            LinearProgressIndicator()
        }
    }
}

@Composable
fun profile(){

}
