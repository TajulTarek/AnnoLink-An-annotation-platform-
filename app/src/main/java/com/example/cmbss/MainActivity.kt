package com.example.cmbss

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cmbss.ui.theme.CmbssTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity(), SignInCallBack {
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth


    val userList: MutableList<Map<String, Any>> = mutableListOf()
    val signInCallBack:SignInCallBack=this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        setContent {
            CmbssTheme {
                /*var nambe by remember { mutableStateOf("") }
                auth = FirebaseAuth.getInstance()
                db = FirebaseFirestore.getInstance()
                db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        if (!result.isEmpty()) {
                            // Get the first document
                            val firstDocument = result.documents[1]

                            // Get the value of the "first" field (assuming it's a String)
                            val firstName = firstDocument.getString("first")

                            // Update the TextView with the first name
                            nambe = "First Name: $firstName"
                        } else {
                            // Handle the case where there are no users
                            nambe = "No users found"
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents.", exception)
                    }   */



                //signin(signInCallBack)
                SignUp2()
            }
        }
    }
    public override fun onStart() {
        super.onStart()
        //super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
        /*nambe="kk"
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val userData: Map<String, Any> = document.data
                    userList.add(userData)
                    println(nambe)
                }
                for (user in userList) {
                    val firstName = user["first"] as String
                    val lastName = user["last"] as String
                    nambe = "$firstName $lastName kkkkk"
                }

            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }*/

    }
    fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
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



    fun signIn(email: String, password: String) {
        if ( email=="" || password ==""){

        }
        else {
            // [START sign_in_with_email]
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                        val intent= Intent(this@MainActivity,studenthomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        updateUI(null)
                    }
                }
        }
        // [END sign_in_with_email]
    }
    private fun sendEmailVerification() {
        // [START send_email_verification]
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }
        // [END send_email_verification]
    }

    private fun updateUI(user: FirebaseUser?) {
    }

    private fun reload() {
        val intent= Intent(this@MainActivity,studenthomeActivity::class.java)
        startActivity(intent)
    }
    override fun OnSignUp() {
        val intent= Intent(this@MainActivity,signupactivity::class.java)
        startActivity(intent)
    }

    override fun OnSignIn(email: String,password: String) {
        signIn(email,password)
        //createAccount(email,password)

    }
    override fun OnForgetPassword() {
        val intent= Intent(this@MainActivity,ForgetPasswordActivity::class.java)
        startActivity(intent)
        // Create a new user with a first and last name
         /*val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1050
        )

// Add a new document with a generated ID
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }*/
    }
}

