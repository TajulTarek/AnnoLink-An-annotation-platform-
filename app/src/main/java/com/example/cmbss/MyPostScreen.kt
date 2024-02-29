package com.example.cmbss

import android.R.color
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun MyPostScreen(myPostsCallBack: MyPostsCallBack,myPosts:List<Post>) {
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
                .background(bgcolor),
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
                Text(
                    modifier=Modifier.fillMaxWidth(),
                    text = "My Posts",
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(offwhite)
            ) {
                items(myPosts){
                    ColumnItem(
                        modifier = Modifier,
                        id = it.id,
                        title = it.title,
                        time = it.time,
                        deadline = it.time,
                        isAvailable = it.isAvailable,
                        myPostsCallBack,
                        it.size
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
               title: String,
               time:String,
               deadline:String,
               isAvailable:Boolean,
               myPostsCallBack: MyPostsCallBack,
               size:Int

) {

    val offwhite=Color(0xffe5bdc4)
    val db = Firebase.firestore

    Card(
        modifier
            .padding(start = 10.dp, end = 10.dp, top = 4.dp, bottom = 4.dp)
            .wrapContentSize()
            .clickable {
                myPostsCallBack.OnCardClick(id)
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
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Total Applicants: $size",
                        style = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            lineHeight = 20.sp,
                            fontStyle = FontStyle.Italic
                        ),
                        color=Color.Black
                    )

                    Spacer(modifier = Modifier.weight(1f))
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
                        ),
                        color=Color.Black
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

                BlinkingText(
                    text = "Tap to see the applicants",
                    color = Color.Gray,
                    size = 12.0f
                )

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
@Composable
fun BlinkingText(
    text: String,
    color:Color,
    size: Float
) {

    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.Bold,
            color = color,
            fontSize = size.sp
        ),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

data class Post(
    val id: String,
    val title: String,
    val time:String,
    val isAvailable: Boolean,
    val size:Int
)
@Preview
@Composable
fun MyPostScreenPreview() {
}