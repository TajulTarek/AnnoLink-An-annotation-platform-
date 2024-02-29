package com.example.cmbss

import android.net.Uri

interface SignUp2CallBack {
    fun OnCreate(email: String,password: String,fullname: String,uniName:String,
                 stuidentId:String,phone:String,deptartment:String,
                 semester:String, github:String,linkedin:String,selectedImageUri: Uri?)
    fun BackToSignUp()
}