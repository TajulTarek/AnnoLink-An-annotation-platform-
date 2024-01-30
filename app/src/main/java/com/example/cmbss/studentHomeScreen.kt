package com.example.cmbss

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.ReportGmailerrorred
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.search.SearchBar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun studentHome (studentHomeCallBack: StudentHomeCallBack) {
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
                        val postId=document.getString("id")?:""
                        val jobtitle = document.getString("title")?:""
                        val jobdescription=document.getString("description")?:""

                        joblist+=JobPost(postId,jobtitle,jobdescription)
                        println("xxxxxxxxx $postId")

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
                            it.description
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
    titles: String,
    descriptions: String
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
        Row(
            modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            Column(modifier.padding(12.dp)) {
                Text(text = titles, fontSize = 24.sp, fontWeight = FontWeight.Bold,color=Color.Black)
                Text(text = descriptions, fontSize = 18.sp,color=Color.Black)

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
    val description: String
)
@Composable
fun profile(){

}
