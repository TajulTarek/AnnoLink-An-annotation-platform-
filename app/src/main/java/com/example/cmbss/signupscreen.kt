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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun signup(signUpCallBack: SignUpCallBack) {
    val colorWhite = colorResource(id = R.color.white)
    val bgcolor= Color(0xFF3EA7D7)
    val logincolor=Color(0xFF101361)
    val deepBlueColor = Color(0xFF00008B)
    var nameValue by remember { mutableStateOf("") }
    val emailValue = remember { mutableStateOf("") }
    val phoneValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }
    val confirmPasswordValue = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    val confirmPasswordVisibility = remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(bgcolor),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.signupphoto),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(150.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 300.dp) //total height=850dp
                .align(Alignment.BottomCenter)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
                )
        ){
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp)){
                Text(
                    text = "Get Started",
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
                    label = { Text(text = "Enter Your FullName") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary,
                        cursorColor = Primary,
                        textColor = Color.Black
                    ),
                    placeholder = { Text(text = "Name") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    singleLine = true,
                    value = nameValue,
                    onValueChange = {
                        nameValue = it
                    },
                    leadingIcon = {
                        Icon(painter = painterResource(id = R.drawable.person_icon), contentDescription ="",
                            tint=Color.Black)
                    },
                    shape = RoundedCornerShape(16.dp)


                )

                Spacer(modifier = Modifier.padding(10.dp))

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
                    value = emailValue.value,
                    onValueChange = {
                        emailValue.value = it
                    },
                    leadingIcon = {
                        Icon(painter = painterResource(id = R.drawable.icon_email), contentDescription ="",
                            tint=Color.Black)
                    },
                    shape = RoundedCornerShape(16.dp)

                )
                Spacer(modifier = Modifier.padding(10.dp))


                OutlinedTextField(
                    label = { Text(text = if(passwordError)"Password must contain at least 6 characters" else "") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Primary,
                        focusedLabelColor = Primary,
                        cursorColor = Primary,
                        textColor = Color.Black
                    ),
                    placeholder = { Text(text = "Password") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    value = passwordValue.value,
                    onValueChange = {
                        passwordValue.value = it
                        passwordError = passwordValue.value.length < 6
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
                    visualTransformation = if (passwordVisibility.value) VisualTransformation.None
                    else PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.padding(20.dp))

                Button(onClick = {
                           signUpCallBack.OnSignUp(nameValue,emailValue.value,passwordValue.value)
                }, modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp)) {
                    Text(text = "Sign up",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Normal,
                            letterSpacing = 1.sp
                        ))
                }

                Spacer(modifier = Modifier.padding(20.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Adjust the space between the lines and the text "or"
                    Text(text = "or",
                        color = colorResource(id = R.color.white)
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Adjust the space between the text "or" and the lines
                    Divider(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                Image(painter = painterResource(id = R.drawable.google_icon), contentDescription = "")
                Spacer(modifier = Modifier.padding(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Already have an account?",
                        style= TextStyle(
                            color=Color.Black.copy(alpha=0.5f),
                            fontSize=16.sp
                        ))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Sign in",
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.clickable {
                            signUpCallBack.OnSignIn()
                        }
                    )
                }
            }

        }


    }
}
@Preview(showBackground = true)
@Composable
fun pre() {
    //signup(signUpCallBack)
}