package com.example.cmbss



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.rememberImagePainter
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


data class Profile(
    val photoUrl: String,
    val name: String,
    val university: String,
    val department: String,
    val semester:String,
    val studentId: String,
    val linkedinUrl: String,
    val githubUrl: String,
    val phoneNumber: String,
)


@Composable
fun ProfileSection(title: String, content: String) {
    Card(
        modifier= Modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .wrapContentSize()
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(4.dp)
    ){
        Column(modifier = Modifier.padding(vertical = 6.dp, horizontal = 8.dp)) {
            Text(
                text = title,
                modifier = Modifier
                    .padding(),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Normal
                )
            )

            Text(text = content,
                modifier=Modifier.padding(top=4.dp),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.02.sp,
                    color = Color.Black
                )
                )
        }
    }
}
@Composable
fun MyProfile(myProfileCallBack: MyProfileCallBack,Id: String) {
    val bgcolor = Color(0xFF3EA7D7)
    val offwhite = Color(0xfffde4f2)
    val iconbg=Color(0xffe3cdd9)
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val db= Firebase.firestore
    val userCollectionRef = db.collection("users")
    val userDocumentRef = userCollectionRef.document(Id)
    var profilePictureUrl by remember {
        mutableStateOf("")
    }
    var fullname by remember {
        mutableStateOf("")
    }
    var university by remember {
        mutableStateOf("")
    }
    var dept by remember {
        mutableStateOf("")
    }
    var semester by remember {
        mutableStateOf("")
    }
    var studentId by remember {
        mutableStateOf("")
    }
    var githubUrl by remember {
        mutableStateOf("")
    }
    var linkedinUrl by remember {
        mutableStateOf("")
    }
    var phoneNumber by remember {
        mutableStateOf("")
    }
    var profile by remember { mutableStateOf(Profile("", "", "", "", "", "", "", "", "")) }
    userDocumentRef.get()
        .addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                profilePictureUrl = documentSnapshot.getString("dp") ?: ""
                fullname=documentSnapshot.getString("fullname")?:""
                university=documentSnapshot.getString("uniName")?:""
                dept=documentSnapshot.getString("deptartment")?:""
                semester=documentSnapshot.getString("semester")?:""
                studentId=documentSnapshot.getString("studentId")?:""
                githubUrl=documentSnapshot.getString("github")?:""
                linkedinUrl=documentSnapshot.getString("linkedin")?:""
                phoneNumber=documentSnapshot.getString("phone")?:""
                profile=Profile(profilePictureUrl,fullname,university,
                    dept,semester, studentId ,githubUrl,linkedinUrl, phoneNumber)
            } else {
                // Document does not exist, handle accordingly
            }

        }
    val painter = rememberImagePainter(data = profilePictureUrl, builder = {
        crossfade(true)
        placeholder(R.drawable.person_icon) // Replace with a placeholder image resource
    })

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(offwhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp, vertical = 24.dp)
        ) {
            // Item 1: User Photo and Name

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Load user photo using Coil
                    Image(
                        painter = painter,
                        contentDescription = "Profile picture",
                        modifier = Modifier
                            .size(200.dp)
                            .height(200.dp)
                            .width(200.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = profile.name,
                        style = TextStyle(
                            fontSize = 20.sp, // Adjust the size as needed
                            fontWeight = FontWeight.Bold, // Optionally, you can make it bold
                            fontStyle = FontStyle.Normal,
                            letterSpacing = 0.5.sp
                        ))
                    Spacer(modifier = Modifier.height(10.dp))

                }

            LazyColumn(){

                item {
                    ProfileSection(title = "University:", content = profile.university)
                    ProfileSection(title = "Department:", content = profile.department)
                    ProfileSection(title = "Semester:", content = profile.semester)
                    ProfileSection(title = "Student ID:", content = profile.studentId)
                    ProfileSection(title = "Phone Number:", content = profile.phoneNumber)
                }

                // Item 3: LinkedIn and GitHub Icons
                item {
                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                ,
                            contentAlignment = Alignment.Center,
                        ) {
                            IconButton(
                                onClick = {
                                          myProfileCallBack.OnGithub(githubUrl)
                                     },
                                modifier = Modifier.size(50.dp).border(0.dp, iconbg, shape = RoundedCornerShape(1.dp))
                                    .background(iconbg)
                            ) {
                                Icon(painter = painterResource(id = R.drawable.icons8_github), contentDescription = "LinkedIn"
                                ,modifier=Modifier
                                        .size(40.dp)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = {
                                          myProfileCallBack.OnLinkedin(githubUrl)
                                },
                                modifier = Modifier.size(50.dp).border(0.dp, iconbg, shape = RoundedCornerShape(1.dp))
                                    .background(iconbg)
                            ) {
                                Icon(painter = painterResource(id = R.drawable.icons8_linkedin_48), contentDescription = "GitHub"
                                    ,modifier=Modifier
                                        .size(40.dp)
                                    )
                            }
                        }
                    }

                }

                // Item 4: Bio

            }
            // Item 2: University, Department, Semester, Student ID, Phone Number

        }
    }
}


