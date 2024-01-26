package com.example.cmbss
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(addPostCallBack: AddPostCallBack) {


    var title by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)){
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
                    color = colorResource(id = R.color.black),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.padding(20.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(text = "username") },
                    placeholder = { Text(text = "username") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    value = username,
                    onValueChange = {
                        username = it
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black, // Set the text color to black
                        cursorColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.padding(10.dp))
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
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black, // Set the text color to black
                        cursorColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    )

                )

                Spacer(modifier = Modifier.padding(10.dp))


                OutlinedTextField(
                    label = { Text(text = "Description") },
                    placeholder = { Text(text = "Description") },
                    modifier = Modifier.fillMaxWidth().height(300.dp)
                        .background(color = Color.White),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    value = description,
                    onValueChange = {
                        description = it
                    },

                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black, // Set the text color to black
                        cursorColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    )
                )


                Spacer(modifier = Modifier.padding(20.dp))

                Button(onClick = { addPostCallBack.OnPost(username,title,description) }, modifier = Modifier
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

                Spacer(modifier = Modifier.padding(20.dp))


            }

        }



}
