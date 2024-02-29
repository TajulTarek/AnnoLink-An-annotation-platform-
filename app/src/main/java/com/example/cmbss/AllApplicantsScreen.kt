package com.example.cmbss

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AllApplicantsScreen(allApplicantsCallBack: AllApplicantsCallBack,postId:String){
    val bgcolor = Color(0xFF3EA7D7)
    val offwhite= Color(0xfffde4f2)
    var allApplicants by remember { mutableStateOf<List<applicant>>(emptyList()) }
    val postDocumentReference = FirebaseFirestore.getInstance().collection("posts").document(postId)
    var isloading by remember {
        mutableStateOf(true)
    }

    postDocumentReference.get()
        .addOnSuccessListener { documentSnapshot ->
            val temp=documentSnapshot
            if (documentSnapshot.exists()) {
                val applicantsArray = temp.get("applicants") as? List<String>
                println("uski nakiaa")
                if (applicantsArray != null) {
                    println("Number of Applicants: ${applicantsArray?.size}")

                    for (applicantUid in applicantsArray) {
                        val userDocumentReference = FirebaseFirestore.getInstance().collection("users").document(applicantUid)

                        userDocumentReference.get()
                            .addOnSuccessListener { userDocumentSnapshot ->
                                if (userDocumentSnapshot.exists()) {
                                    val userName = userDocumentSnapshot.getString("fullname")?:""
                                    val deptartment = userDocumentSnapshot.getString("deptartment")?:""
                                    val semester = userDocumentSnapshot.getString("semester")?:""
                                    val dp = userDocumentSnapshot.getString("dp")?:""

                                    allApplicants+=(applicant(applicantUid,userName,deptartment,semester,dp))


                                } else {
                                }
                            }
                            .addOnFailureListener { exception ->
                            }
                    }
                } else {
                }
            } else {
            }
        }
        .addOnFailureListener { exception ->
        }

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray),
                horizontalAlignment = Alignment.Start
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Applicants ",
                    color = Color.White,
                    fontSize = 24.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.White)
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(offwhite)
                ) {
                    items(allApplicants) {
                        ColumnItem(
                            modifier = Modifier,
                            id = it.id,
                            name = it.name,
                            allApplicantsCallBack = allApplicantsCallBack,
                            deptartment = it.deptartment,
                            semester = it.semester,
                            dp=it.dp

                        )
                    }
                }

            }
        }
    }

@Composable
fun ColumnItem(
    modifier: Modifier,
    id:String,
    name:String,
    allApplicantsCallBack: AllApplicantsCallBack,
    deptartment: String,
    semester: String,
    dp: String

) {

    val offwhite=Color(0xffe5bdc4)
    val db = Firebase.firestore
    val painter = rememberImagePainter(data = dp, builder = {
        crossfade(true)
        placeholder(R.drawable.person_icon) // Replace with a placeholder image resource
    })
    var clickEffect by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    Card(
        modifier
            .padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 4.dp)
            .wrapContentSize()
            .height(55.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(0.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            // Top Row
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(55.dp)
                        .background(Color.White)
                        .align(Alignment.CenterVertically)
                ){
                    Image(painter = painter,
                        contentDescription = "",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = name,
                            color = if (clickEffect) Color.Red else Color(0xFF0D47A1),
                            modifier=Modifier.clickable {
                                allApplicantsCallBack.OnOtherProfileClick(id)
                                scope.launch {
                                    clickEffect = true
                                    delay(200)
                                    clickEffect = false
                                }
                            }
                        )

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "$deptartment - $semester",
                            style = TextStyle(
                                color = Color(0xFF4CAF50),
                                        fontSize = 10.sp,
                                lineHeight = 10.sp,
                                fontStyle = FontStyle.Italic
                            )
                        )
                    }
                }


            }
        }


    }
}



data class applicant(
    val id:String,
    val name:String,
    val deptartment:String,
    val semester:String,
    val dp:String
)