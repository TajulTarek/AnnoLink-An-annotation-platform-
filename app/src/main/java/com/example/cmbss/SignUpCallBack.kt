package com.example.cmbss

interface SignUpCallBack {
    fun OnSignUp(fullname: String, email: String, password: String)
    fun OnSignIn()
    fun createAccount(fullname: String,email: String, password: String)

    //abstract fun <FirebaseUser> updateUI(user: FirebaseUser?)

}