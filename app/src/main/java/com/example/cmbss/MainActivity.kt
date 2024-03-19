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
        auth = Firebase.auth
        setContent {
            CmbssTheme {
                signin(signInCallBack)

            }
        }
    }
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload()
        }
    }
    fun createAccount(email: String, password: String) {
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
    }



    fun signIn(email: String, password: String) {
        if ( email=="" || password ==""){

        }
        else {

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
    }
    private fun sendEmailVerification() {

        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->

            }

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
        val t_email=email.trim()
        signIn(t_email,password)

    }
    override fun OnForgetPassword() {
        val intent= Intent(this@MainActivity,ForgetPasswordActivity::class.java)
        startActivity(intent)

    }
}

