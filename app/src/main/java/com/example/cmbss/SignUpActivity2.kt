package com.example.cmbss

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class SignUpActivity2 : AppCompatActivity() ,SignUp2CallBack{
    private lateinit var email:String
    private lateinit var password:String
    private lateinit var fullname:String

    private lateinit var receivedIntent: Intent
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    val signUp2CallBack:SignUp2CallBack=this
    val filename = "${UUID.randomUUID()}.jpg"

    // Get a reference to the Firebase Storage location
    val storageReference: StorageReference = FirebaseStorage.getInstance().getReference("images/$filename")
    override fun onCreate(savedInstanceState: Bundle?) {
        receivedIntent=intent
        email=(receivedIntent.getSerializableExtra("email")as?String)!!
        password=(receivedIntent.getSerializableExtra("password")as?String)!!
        fullname=(receivedIntent.getSerializableExtra("fullname")as?String)!!
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContent {
            SignUp2(signUp2CallBack,email,password,fullname)
        }
    }
    override fun OnCreate(email: String,password: String,fullname: String,uniName:String,
                          studentId:String,phone:String,department:String,
                          semester:String, github:String,linkedin:String,selectedImageUri: Uri?) {

        createAccount(email,password,fullname,
            uniName,studentId,phone,
            department,semester,github,linkedin,selectedImageUri)
    }
    fun createAccount(email: String,password: String,fullname: String,uniName:String,
                      studentId:String,phone:String,deptartment:String,
                      semester:String, github:String,linkedin:String,selectedImageUri: Uri?) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    var userId = user?.uid
                    if (selectedImageUri != null) {
                        Toast.makeText(
                            baseContext,
                            "Please wait. Your account is creating.. ",
                            Toast.LENGTH_LONG,
                        ).show()
                        storageReference.putFile(selectedImageUri)
                            .addOnSuccessListener {
                                storageReference.downloadUrl.addOnSuccessListener { uri ->
                                    val downloadUrl = uri.toString()
                                    storestudent(userId,email,password,fullname,uniName,
                                        studentId, phone, deptartment, semester, github, linkedin,downloadUrl)
                                    val intent= Intent(this@SignUpActivity2,studenthomeActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                            .addOnFailureListener {
                            }
                    }


                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }

            }
        // [END create_user_with_email]
    }

    fun storestudent(userId: String?, email: String, password: String,fullname: String,
                     uniName: String,studentId: String,phone: String,deptartment: String,
                     semester: String,github: String,linkedin: String,downloadUrl:String){
        /*val newuser = hashMapOf(
            "fullname" to fullname,
            "email" to email,
            "password" to password
        )

// Add a new document with a generated ID
        db.collection("users")
            .add(newuser)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            } */




        // Reference to your Firestore collection (replace "users" with your actual collection name)
        val usersCollection = FirebaseFirestore.getInstance().collection("users")

        // Create a new document with the user's UID as the document ID
        val userDocument = usersCollection.document((userId ?: ""))

        // Create a data map with additional user information
        val userData = hashMapOf(
            "email" to email,
            "password" to password,
            "fullname" to fullname,
            "uniName" to uniName,
            "studentId" to studentId,
            "phone" to phone,
            "deptartment" to deptartment,
            "semester" to semester,
            "github" to github,
            "linkedin" to linkedin,
            "dp" to downloadUrl,
            "total_annotate" to 0.0,
            "all_Rating" to 0.0
        )

        // Set the data to the document
        userDocument.set(userData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "User information stored successfully")
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error storing user information", e)
            }
    }

    override fun BackToSignUp() {
        val intent= Intent(this@SignUpActivity2,signupactivity::class.java)
        startActivity(intent)
    }
}
