package com.example.cmbss
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cmbss.R
import com.example.cmbss.ui.theme.Primary
import com.example.cmbss.ui.theme.primaryColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgetPassword() {
    val colorWhite = colorResource(id = R.color.white)
    val nameValue = remember { mutableStateOf("") }
    val emailValue = remember { mutableStateOf("") }
    val phoneValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    val confirmPasswordValue = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    val confirmPasswordVisibility = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                //.heightIn(min = 300.dp) //total height=850dp
                //.align(Alignment.BottomCenter)
                .background(
                    colorResource(id = R.color.purple_500),
                    //shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)){
                Text(
                    text = "Forget Password?",
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                        letterSpacing = 1.sp
                    ),
                    color = colorResource(id = R.color.white),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Please enter your email address to get verification code",
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Normal,
                        letterSpacing = 1.sp
                    ),
                    color = colorResource(id = R.color.white),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.padding(20.dp))


                Spacer(modifier = Modifier.padding(10.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(text = "Enter Your Email") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary,
                        cursorColor = Primary,
                    ),
                    placeholder = { Text(text = "Email") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    value = emailValue.value,
                    onValueChange = {
                        emailValue.value = it
                    },
                    leadingIcon = {
                        Icon(painter = painterResource(id = R.drawable.icon_email), contentDescription ="")
                    }

                )
                Spacer(modifier = Modifier.padding(10.dp))

                Spacer(modifier = Modifier.padding(20.dp))

                Button(onClick = { }, modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp)) {
                    Text(text = "Get Code",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Normal,
                            letterSpacing = 1.sp
                        ))
                }

            }

        }


    }
}

@Preview(showBackground = true)
@Composable
fun preforget() {
    ForgetPassword()
}
