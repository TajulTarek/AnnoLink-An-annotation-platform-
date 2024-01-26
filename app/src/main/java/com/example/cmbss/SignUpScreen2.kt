package com.example.cmbss

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.cmbss.ui.theme.Primary
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUp2() {
    val colorWhite = colorResource(id = R.color.white)
    val bgcolor = Color(0xFF3EA7D7)
    val logincolor = Color(0xFF101361)
    val deepBlueColor = Color(0xFF00008B)
    var photoselect by remember { mutableStateOf("Select a Profile Photo") }
    val nameValue = remember { mutableStateOf("") }
    val uniName = remember { mutableStateOf("") }
    val studentId = remember { mutableStateOf("") }
    var currentSem by remember { mutableStateOf("Select Current Semester") }
    var expandedSem by remember { mutableStateOf(false) }
    val phoneValue = remember { mutableStateOf("") }
    val githubLink = remember { mutableStateOf("") }
    val linkedinLink = remember { mutableStateOf("") }
    val confirmPasswordValue = remember { mutableStateOf("") }
    val passwordVisibility = remember { mutableStateOf(false) }
    val confirmPasswordVisibility = remember { mutableStateOf(false) }
    val semesters = arrayOf("1st Semester",
        "2nd Semester", "3rd Semester", "4th Semester",
        "5th Semester", "6th Semester", "7th Semester", "8th Semester"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgcolor),
        contentAlignment = Alignment.TopCenter
    ) {
        var selectedImageUri by remember {
            mutableStateOf<Uri?>(null)
        }

        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                selectedImageUri = uri
                //upload(selectedImageUri)
                photoselect="Selected"

            }
        )
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(20.dp)

        ) {
            item {
                Button(
                    onClick = {
                        singlePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                        Log.d(TAG, "Entering2")
                        Log.d(TAG, "Selected Image Uri: $selectedImageUri")

                    }, modifier = Modifier.size(150.dp),
                    shape = RoundedCornerShape(10.dp)

                ) {
                    Text(
                        text = photoselect,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Normal,
                            letterSpacing = 1.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.padding(10.dp))
                textfield(ondisplay = "Enter Fullname", value = nameValue,placeholder="Name")
                textfield(ondisplay = "Enter University Name", value = uniName, placeholder = "University")
                textfield(ondisplay = "Enter Your ID", value = studentId, placeholder = "your Id")
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White),
                    label = { Text(text = "Enter phone number") },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.White,
                        focusedLabelColor = Primary,
                        cursorColor = Primary,
                    ),
                    placeholder = { Text(text = "Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                    singleLine = true,
                    value = phoneValue.value,
                    onValueChange = {
                        phoneValue.value = it
                    },
                    textStyle = LocalTextStyle.current.copy(color = Color.Black)

                )
                Spacer(modifier = Modifier.padding(10.dp))

                var expanded by remember { mutableStateOf(false) }
                var selectedSubject by remember { mutableStateOf("Select Your Department") }
                val bgcolor = Color(0xFF3EA7D7)
                val subjects = arrayOf("Department of Computer Science & Engineering (CSE)",
                    "Department of Architecture (ARC)",
                    "Department of Chemical Engineering & Polymer Science (CEP)",
                    "Department of Civil & Environmental Engineering (CEE)",
                    "Department of Electrical & Electronic Engineering (EEE)",
                    "Department of Food Engineering & Tea Technology (FET)",
                    "Department of Industrial & Production Engineering (IPE)",
                    "Department of Mechanical Engineering (MEE)",
                    "Department of Petroleum & Mining Engineering (PME)"
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = {expanded= !expanded}

                    ) {
                        TextField(value = selectedSubject, onValueChange = {}, readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                            },
                            modifier=Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded=false }) {
                            subjects.forEach {
                                DropdownMenuItem(
                                    text = { Text(text=it) },
                                    onClick = {
                                        selectedSubject=it
                                        expanded=false
                                    }//,
                                    //modifier=Modifier
                                      //  .background(Color.White)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(10.dp))
                ////
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ExposedDropdownMenuBox(expanded = expandedSem, onExpandedChange = {expandedSem= !expandedSem}

                    ) {
                        TextField(value = currentSem ,onValueChange = {}, readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSem)
                            },
                            modifier=Modifier.menuAnchor()
                        )
                        ExposedDropdownMenu(expanded = expandedSem, onDismissRequest = { expandedSem=false }) {
                            semesters.forEach {
                                DropdownMenuItem(
                                    text = { Text(text=it) },
                                    onClick = {
                                        currentSem=it
                                        expandedSem=false
                                    }//,
                                    //modifier=Modifier
                                    //  .background(Color.White)
                                )

                            }
                        }
                    }

                }
                Spacer(modifier = Modifier.padding(10.dp))
                textfield(ondisplay = "Enter Github Link", value = githubLink, placeholder = "github")
                textfield(ondisplay = "Enter linkedin Link", value = linkedinLink, placeholder = "linkedin")

                Button(onClick = {

                }, modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text(text = "Create",
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
fun signpre(){
    SignUp2()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textfield(ondisplay:String,value: MutableState<String>,placeholder: String){
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White),
        label = { Text(text = ondisplay) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            focusedLabelColor = Primary,
            cursorColor = Primary,
        ),
        placeholder = { Text(text = placeholder) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        value = value.value,
        onValueChange = {
            value.value = it
        },
        /*leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.icon_email),
                contentDescription = "",
                tint = Color.Black
            )
        },*/
        textStyle = LocalTextStyle.current.copy(color = Color.Black)

    )
    Spacer(modifier = Modifier.padding(10.dp))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown() {


}


/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(userProfile: UserProfile,profileCallBack: ProfileCallBack) {
    val auth= Firebase.auth
    val currentUser=auth.currentUser
    val density = LocalDensity.current.density
    val bottomPadding = with(LocalView.current) {
        if (isAttachedToWindow) {
            val insets = ViewCompat.getRootWindowInsets(this)?.systemGestureInsets
            insets?.bottom?.toFloat()?.div(density) ?: 0f
        } else {
            0f
        }
    }
    val items = listOf(
        BottomNavigationItem(
            title = "Feed",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Post",
            selectedIcon = Icons.Filled.Add,
            unselectedIcon = Icons.Outlined.Add
        ),
        BottomNavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        ),
        BottomNavigationItem(
            title = "Profile",
            selectedIcon = Icons.Filled.Face,
            unselectedIcon = Icons.Outlined.Face
        ),
    )

    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(3)
    }

    val initialIndex = remember { selectedItemIndex }

    Scaffold(
        bottomBar = {
            NavigationBar() {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            // navController.navigate(item.title)
                            if(selectedItemIndex==1){
                                profileCallBack.gotoPost()
                            }
                            else if(selectedItemIndex==0){
                                profileCallBack.gotoFeed()
                            }
                        },
                        label = {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = true,
                        icon = {

                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )

                        }
                    )
                }
            }
        }
    ) {innerPadding ->
        var selectedImageUri by remember {
            mutableStateOf<Uri?>(null)
        }

        val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri ->
                selectedImageUri = uri
                upload(selectedImageUri)

            }
        )
        LazyColumn(

        ) {
            item {
                ProfilePicture(userProfile.profilePictureUrl)
                Spacer(modifier = Modifier.height(16.dp))
                ElevatedButton(onClick = { singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    Log.d(TAG,"Entering2")
                    Log.d(TAG, "Selected Image Uri: $selectedImageUri")

                },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 10.dp
                    )
                ) {
                    Icon(imageVector = Icons.Default.AddPhotoAlternate, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Change Profile Picture", color = Color.Black)
                }
                Spacer(modifier = Modifier.height(16.dp))
                UserDetailCard(
                    icon = Icons.Default.Person,
                    label = "Name",
                    value = userProfile.name,
                    onClick={}
                )
                UserDetailCard(
                    icon = Icons.Default.Person,
                    label = "Username",
                    value = userProfile.username,
                    onClick={}
                )

                EmailCard(
                    icon = Icons.Default.Email,
                    label = "Email",
                    value = userProfile.email,
                    onClick={profileCallBack.sendVerificationMail()},
                    currentUser=currentUser
                )

                UserDetailCard(
                    icon = Icons.Default.Phone,
                    label = "Phone Number",
                    value = userProfile.phoneNumber,
                    onClick={}
                )

                UserDetailCard(
                    icon = Icons.Default.TripOrigin,
                    label = "Completed Trips",
                    value = userProfile.completedTrips.toString(),
                    onClick={}
                )

                UserDetailCard(
                    icon = Icons.Default.WorkOutline,
                    label = "Running Trips",
                    value = userProfile.runningTrips.toString(),
                    onClick={
                        profileCallBack.myRunningTrips()
                    }
                )

                UserDetailCard(
                    icon = Icons.Default.Star,
                    label = "User Rating",
                    value = userProfile.userRating.toString(),
                    onClick={}
                )
                Button(
                    onClick = {

                    }, shape = CutCornerShape(10.dp)
                ) {
                    Text(
                        text = "LOGOUT",
                        style = TextStyle(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Medium,
                            fontStyle = FontStyle.Normal,
                            letterSpacing = 2.sp,
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

    }



}

fun upload(selectedImageUri: Uri?) {

    val db: FirebaseFirestore=Firebase.firestore
    val storage: FirebaseStorage= Firebase.storage
    val auth: FirebaseAuth=Firebase.auth
    val storageRef: StorageReference = storage.reference
    val currentUser = auth.currentUser
    val uid = currentUser?.uid
    val fileName = uid+"_profile_image.jpg"
    val imageRef: StorageReference = storageRef.child(fileName)
    // Upload the image to Firebase Storage
    imageRef.putFile(selectedImageUri!!)
        .addOnSuccessListener { taskSnapshot ->
            // Image uploaded successfully, now you can get the download URL
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // The URI contains the download URL of the uploaded image
                val downloadUrl = uri.toString()
                val client= hashMapOf(
                    "Photo" to downloadUrl
                )
                if (uid != null) {
                    db.collection("Clients").document(uid)
                        .set(client, SetOptions.merge())
                        .addOnSuccessListener { Log.d(ContentValues.TAG, "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }
                }


            }
        }
        .addOnFailureListener { exception ->
            // Handle the failure of the image upload
            Log.e(ContentValues.TAG, "Error uploading image to Firebase Storage: ${exception.message}")
        }
}

@Composable
fun EmailCard(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit,
    currentUser: FirebaseUser?
){
    Card(
        modifier= Modifier
            //.padding(top=8.dp)
            .padding(4.dp)
            .wrapContentSize()
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(10.dp)
        /*modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .clickable(onClick = onClick) */// Updated clickable modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = icon, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label, style = MaterialTheme.typography.bodyLarge)
                Row (
                    modifier=Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    if (currentUser != null) {
                        BlinkingButton(
                            text = if(currentUser.isEmailVerified)"Verified" else "Verify your mail",
                            color = if(currentUser.isEmailVerified)Color(0xFF008B8B) else Color.Red,
                            onClick=onClick
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun UserDetailCard(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Card(
        modifier= Modifier
            //.padding(top=8.dp)
            .padding(4.dp)
            .wrapContentSize()
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(10.dp)
        /*modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .clickable(onClick = onClick) */// Updated clickable modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = icon, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = label, style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}


@Composable
fun ProfilePicture(profilePictureUrl: String?) {
    Box(
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
    ) {
        // Use Coil's rememberImagePainter to load the image from the URL
        val painter = rememberImagePainter(data = profilePictureUrl, builder = {
            crossfade(true)
            placeholder(R.drawable.pp) // Replace with a placeholder image resource
        })

        Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .clip(MaterialTheme.shapes.medium)
        )
    }
}




@Composable
fun ProfileScreen(
    profilePictureUrl: String?,
    name: String,
    username: String,
    email: String,
    phoneNumber: String,
    completedTrips: Int,
    runningTrips: Int,
    userRating: Double,
    profileCallBack: ProfileCallBack) {
    val userProfile = UserProfile(
        profilePictureUrl = profilePictureUrl,
        name = name,
        username = username,
        email = email,
        phoneNumber = phoneNumber,
        completedTrips = completedTrips,
        runningTrips = runningTrips,
        userRating = userRating
    )
    UserProfileScreen(userProfile = userProfile,profileCallBack=profileCallBack)
}*/