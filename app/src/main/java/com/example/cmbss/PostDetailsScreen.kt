package com.example.cmbss

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.cmbss.ui.theme.Primary
import com.example.cmbss.ui.theme.primaryColor
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetails(postId:String){
    val db = Firebase.firestore
    val collectionRef = db.collection("posts")
    val documentRef = collectionRef.document(postId)
    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    documentRef.get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val postId= document.id
                title= document.getString("title").toString()
                description=document.getString("description").toString()
                // Now you have the document ID (postId)
                println("Document ID: $postId\n $title   $description")
            } else {
                println("Document does not exist")
            }
        }
        .addOnFailureListener { exception ->
            println("Error getting document: $exception")
        }

    val sky = Color(0xFFA7E3FF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = sky)
    ) {
        Icon(imageVector = Icons.Filled.ArrowBack,
            contentDescription = "d",
            tint=Color.Black,
            modifier = Modifier.clickable {

            }
                .padding(10.dp)
                .size(30.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 770.dp) //total height=850dp
                .align(Alignment.BottomCenter)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)){
                Text(
                    text = title,
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                        letterSpacing = 1.sp
                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Text(text=description,
                    modifier = Modifier
                        .padding(20.dp)
                        .clickable {
                            //signInCallBack.OnForgetPassword()
                        }
                )

                Spacer(modifier = Modifier.padding(20.dp))

                Button(onClick = {

                    }, modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(text = "Apply",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Normal,
                            letterSpacing = 1.sp,
                        ))
                }


            }

        }


    }
}

@Composable
@Preview
fun dpre(){
    //PostDetails()
}