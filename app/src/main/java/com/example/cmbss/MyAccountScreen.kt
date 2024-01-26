package com.example.cmbss

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cmbss.ui.theme.Primary
import com.example.cmbss.ui.theme.primaryColor
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAccount(myAccountCallBack: MyAccountCallBack) {
    val auth = FirebaseAuth.getInstance()
    val user = Firebase.auth.currentUser
    var  uid=""
    user?.let {
        uid = it.uid
    }


    // Write a message to the database
    /*val database = Firebase.database
    val myRef = database.getReference("message")

    myRef.setValue("Hello, World!")*/
    val db = Firebase.firestore
    val usersCollection= db.collection("users")

    val colorWhite = colorResource(id = R.color.white)
    val greenColor = Color.Green
    val textSize = 24.sp
    val paddingSize = 12.dp
    var emailValue by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    val confirmPasswordValue = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    val confirmPasswordVisibility = remember { mutableStateOf(false) }

    DisposableEffect(true) {
        val docRef = usersCollection.document(uid) // Replace with the actual document ID
        docRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    fullName = document.getString("fullname") ?: ""
                    emailValue = document.getString("email") ?: ""
                    passwordValue = document.getString("password")?:""
                } else {
                    // Document does not exist
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }

        onDispose { /* cleanup */ }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = fullName,
                fontSize = textSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = paddingSize)
                    .background(Color.Black)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.heightIn(10.dp))

            Text(
                text = emailValue,
                fontSize = textSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = paddingSize)
                    .background(Color.Black)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.heightIn(10.dp))

            Text(
                text = passwordValue,
                fontSize = textSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = paddingSize)
                    .background(Color.Black)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.heightIn(10.dp))

            Button(
                onClick = {
                    myAccountCallBack.OnLogOut()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = paddingSize)
            ) {
                Text("Log Out")
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyAccountPre() {
    //MyAccount()
}
