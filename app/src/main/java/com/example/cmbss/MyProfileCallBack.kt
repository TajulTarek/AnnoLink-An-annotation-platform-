package com.example.cmbss

interface MyProfileCallBack {
    fun OnGithub(url:String)
    fun OnLinkedin(url: String)
    fun OpenChat(currentuserId:String,oppositeId:String)
}