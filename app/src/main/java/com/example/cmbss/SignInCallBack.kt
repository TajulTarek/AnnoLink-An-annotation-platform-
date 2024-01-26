package com.example.cmbss

interface SignInCallBack {
    fun OnSignUp()
    fun OnSignIn(email: String,password: String)

    fun OnForgetPassword()

}