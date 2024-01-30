package com.example.cmbss
import android.content.ContentValues.TAG
import android.util.Log
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.cmbss.ui.theme.Primary
import com.example.cmbss.ui.theme.primaryColor
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun signin(signInCallBack: SignInCallBack) {


















    val colorWhite = colorResource(id = R.color.white)
    val bgcolor= Color(0xFF3EA7D7)
    val logincolor=Color(0xFF101361)
    val deepBlueColor = Color(0xFF00008B)
    var emailValue by remember { mutableStateOf("") }
    val phoneValue = remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    val confirmPasswordValue = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    val confirmPasswordVisibility = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = bgcolor),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.mipmap.dp_foreground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(200.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 570.dp) //total height=850dp
                .align(Alignment.BottomCenter)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)){
                Text(
                    text = "Login",
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Normal,
                        letterSpacing = 1.sp
                    ),
                    color = deepBlueColor,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.padding(20.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = { Text(text = "Enter Your Email") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary,
                        cursorColor = Primary,
                        textColor = Color.Black
                    ),
                    placeholder = { Text(text = "Email") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    value = emailValue,
                    onValueChange = {
                        emailValue = it
                    },
                    leadingIcon = {
                        Icon(painter = painterResource(id = R.drawable.icon_email), contentDescription ="",
                            tint=Color.Black)
                    },
                    shape = RoundedCornerShape(16.dp)

                )

                Spacer(modifier = Modifier.padding(10.dp))


                OutlinedTextField(
                    label = { Text(text = "Password") },
                    placeholder = { Text(text = "Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    value = passwordValue,
                    onValueChange = {
                        passwordValue = it
                    },
                    leadingIcon = {
                        Icon(painter = painterResource(id = R.drawable.password_lock), contentDescription ="",
                            tint=Color.Black)
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisibility.value = !passwordVisibility.value
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.password_icon),contentDescription ="",
                                tint = if (passwordVisibility.value) primaryColor else Color.Gray
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary,
                        cursorColor = Primary,
                        textColor = Color.Black
                    ),
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                    else PasswordVisualTransformation()

                )
                Text(text="Forget Password?",
                    modifier = Modifier
                        .padding(start = 195.dp, top = 10.dp)
                        .clickable {
                            signInCallBack.OnForgetPassword()
                        }
                    ,
                    style = TextStyle(
                        fontSize = 15.sp
                    )

                )

                Spacer(modifier = Modifier.padding(20.dp))

                Button(onClick = { signInCallBack.OnSignIn(emailValue,passwordValue) }, modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp)) {
                    Text(text = "Sign in",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Normal,
                            letterSpacing = 1.sp,
                        ))
                }

                Spacer(modifier = Modifier.padding(15.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Adjust the space between the lines and the text "or"
                    Text(text = "or",
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Adjust the space between the text "or" and the lines
                    Divider(
                        color = Color.Gray,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))

                Image(painter = painterResource(id = R.drawable.google_icon), contentDescription = "")
                Spacer(modifier = Modifier.padding(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Didn't have an account?",
                        style= TextStyle(
                            color=Color.Black.copy(alpha=0.5f),
                            fontSize=16.sp
                        ))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sign up",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            //signInCallBack.createAccount(emailValue,passwordValue)
                            signInCallBack.OnSignUp()
                        }
                    )
                }
            }

        }


    }
}
@Preview(showBackground = true)
@Composable
fun presignin() {
    //signup()
}