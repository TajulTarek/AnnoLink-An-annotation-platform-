package com.example.cmbss



import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.google.firebase.auth.FirebaseAuth
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
    var email by remember {
        mutableStateOf("")
    }
    var total_annotate by remember {
        mutableStateOf(0.0)
    }
    var all_Rating by remember {
        mutableStateOf(0.0)
    }
    var avg_Rating by remember {
        mutableStateOf(0.0)
    }
    var total_annotate_int by remember {
        mutableStateOf(0)
    }
    val currentUser = FirebaseAuth.getInstance().currentUser
    val currentuserId = currentUser?.uid

    var profile by remember { mutableStateOf(Profile("", "", "", "", "", "", "", "", "")) }
    userDocumentRef.get()
        .addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                profilePictureUrl = documentSnapshot.getString("dp") ?: ""
                fullname=documentSnapshot.getString("fullname")?:""
                email=documentSnapshot.getString("email")?:""
                university=documentSnapshot.getString("uniName")?:""
                dept=documentSnapshot.getString("deptartment")?:""
                semester=documentSnapshot.getString("semester")?:""
                studentId=documentSnapshot.getString("studentId")?:""
                githubUrl=documentSnapshot.getString("github")?:""
                linkedinUrl=documentSnapshot.getString("linkedin")?:""
                phoneNumber=documentSnapshot.getString("phone")?:""
                total_annotate= (documentSnapshot.getDouble("total_annotate")?:0) as Double
                all_Rating= (documentSnapshot.getDouble("all_Rating")?:0) as Double
                avg_Rating=all_Rating/total_annotate
                total_annotate_int=total_annotate.toInt()

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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = "Total Annotate: ${total_annotate.toInt()}",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                fontStyle = FontStyle.Italic
                            )
                        )
                        Spacer(modifier = Modifier.width(12.dp))

                        val singleStarIcon = Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null, // Set to null for decorative icons
                            tint = Color.Yellow,
                            modifier = Modifier.size(24.dp) // Adjust star size as needed
                        )

                        // Display the star icon along with the "Rating" text
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Rating: ${String.format("%.1f", avg_Rating)}",
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp,
                                    fontStyle = FontStyle.Italic // Add italic style
                                )
                            )
                            singleStarIcon // Display the single star icon
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    if(currentuserId!=Id){
                        Box(
                            modifier = Modifier.height(35.dp)
                                .border(2.dp,Color.White, RoundedCornerShape(5.dp))
                                .clickable {
                                    if (currentuserId != null) {
                                        myProfileCallBack.OpenChat(currentuserId,Id)
                                    }
                                }
                            ,
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center) {
                                Icon(modifier=Modifier.size(35.dp).padding(horizontal = 5.dp),
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send Message",
                                    tint = bgcolor
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(modifier = Modifier.padding(end = 5.dp),
                                    text = "Send Message",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            }
                        }

                    }

                }

            LazyColumn(){

                item {
                    ProfileSection(title = "Email :", content = email)
                    ProfileSection(title = "University :", content = profile.university)
                    ProfileSection(title = "Department :", content = profile.department)
                    ProfileSection(title = "Semester :", content = profile.semester)
                    ProfileSection(title = "Student ID :", content = profile.studentId)
                    ProfileSection(title = "Phone Number :", content = profile.phoneNumber)

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
                                modifier = Modifier
                                    .size(50.dp)
                                    .border(0.dp, iconbg, shape = RoundedCornerShape(1.dp))
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
                                          myProfileCallBack.OnGithub(linkedinUrl)
                                },
                                modifier = Modifier
                                    .size(50.dp)
                                    .border(0.dp, iconbg, shape = RoundedCornerShape(1.dp))
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

@Composable
fun RatingStars(rating: Double) {
    // Calculate the number of filled stars (assuming 5-star rating system)
    val numFilledStars = (rating / 2).toInt()

    // Create a list of star icons based on the rating
    val starIcons = List(5) {
        Icon(
            imageVector = if (it < numFilledStars) Icons.Default.Star else Icons.Default.StarBorder,
            contentDescription = null, // Set to null for decorative icons
            tint = Color.Yellow.copy(alpha = if (it < numFilledStars) 1f else 0.5f), // Adjust opacity
            modifier = Modifier.size(24.dp) // Adjust star size as needed
        )
    }

    // Display the star icons in a Row
    Row {
        starIcons.forEach { icon ->
            icon // Display the star icon
        }
    }
}