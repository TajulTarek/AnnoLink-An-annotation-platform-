package com.example.cmbss

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.cmbss.ui.theme.CmbssTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class signupactivity : ComponentActivity() , SignUpCallBack {

    val db = Firebase.firestore
    val signUpCallBack:SignUpCallBack=this
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            CmbssTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    signup(signUpCallBack)
                }
            }
        }
    }

    override fun createAccount(fullname: String,email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    val userId = user?.uid
                    updateUI(user)
                    storestudent(userId,fullname,email,password)
                    val intent= Intent(this@signupactivity,MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun updateUI(user: FirebaseUser?) {

    }

    fun storestudent(userId: String?, fullname: String, email: String, password: String){
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
            "fullname" to fullname,
            "email" to email,
            "password" to password
        )

        // Set the data to the document
        userDocument.set(userData)
            .addOnSuccessListener {
                Log.d(TAG, "User information stored successfully")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error storing user information", e)
            }
    }



    override fun OnSignIn() {
        val intent= Intent(this@signupactivity,MainActivity::class.java)
        startActivity(intent)
    }
    override fun OnSignUp(fullname: String, email: String, password: String) {
        val intent= Intent(this@signupactivity,SignUpActivity2::class.java)
        intent.putExtra("email",email)
        intent.putExtra("password",password)
        intent.putExtra("fullname",fullname)
        startActivity(intent)
        //createAccount(fullname,email,password)

    }
}
