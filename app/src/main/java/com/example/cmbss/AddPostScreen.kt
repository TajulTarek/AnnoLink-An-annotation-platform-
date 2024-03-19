package com.example.cmbss
import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen() {
    val calendar=Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("HH:mm:ss dd:MM:yyyy", Locale.getDefault())
    var datetime="datetime"
    val c=Calendar.getInstance()
    val year=c.get(Calendar.YEAR)
    val month=c.get(Calendar.MONTH)
    val day=c.get(Calendar.DAY_OF_MONTH)
    val context= LocalContext.current
    var deadline by remember{
        mutableStateOf("dd - mm - yy")
    }
    val datepicker=DatePickerDialog(
        context,{ d,year1,month1,day1->
            val month=month1+1
            val finalday=String.format("%02d",day1)
            val finalmonth=String.format("%02d",month)
            deadline="$finalday:$finalmonth:$year1"
        },year,month,day
    )

    var title by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var salary by remember { mutableStateOf("") }
    var qualifications by remember { mutableStateOf("") }
    val bgcolor = Color(0xFF3EA7D7)
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
                    color = bgcolor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.padding(10.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White),
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
                            tint = Color.Black
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.Black, // Set the text color to black
                        cursorColor = Color.Black,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black
                    )

                )

                Spacer(modifier = Modifier.padding(10.dp))

                Box(modifier=Modifier.fillMaxWidth().border(
                    width = 1.dp,
                    color = Color.Black
                )
                    .height(50.dp)
                    .clickable {
                        datepicker.show()
                    }){
                    Text(modifier=Modifier.padding(top=10.dp),text=deadline)
                }

                Spacer(modifier = Modifier.padding(10.dp))

                    OutlinedTextField(
                    label = { Text(text = "Description") },
                    placeholder = { Text(text = "Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
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

            Spacer(modifier = Modifier.padding(10.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White),
                label = { Text(text = "Salary") },
                placeholder = { Text(text = "Salary") },
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
                    textColor = Color.Black, // Set the text color to black
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black
                )

            )

            Spacer(modifier = Modifier.padding(5.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(color = Color.White),
                label = { Text(text = "Desired qualification") },
                placeholder = { Text(text = "qualification") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                value = qualifications,
                onValueChange = {
                    qualifications = it
                },

                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.Black, // Set the text color to black
                    cursorColor = Color.Black,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black
                )

            )


                Spacer(modifier = Modifier.padding(20.dp))

                Button(onClick = {
                    val postedTime = dateFormat.format(android.icu.util.Calendar.getInstance().time)
                    //addPostCallBack.OnPost(title,deadline,description,salary,qualifications,postedTime)
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

                Spacer(modifier = Modifier.padding(20.dp))


            }

        }



}