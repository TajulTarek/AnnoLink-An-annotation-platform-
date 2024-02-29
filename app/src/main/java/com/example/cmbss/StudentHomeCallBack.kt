package com.example.cmbss

interface StudentHomeCallBack {
    fun OnAddPost()
    fun OnSignOut()
    fun OnPostClick(id:String,title:String,description:String,deadline:String,salary:String,fullname:String,qualification:String)
    fun OnMyPosts()
    fun OnMyProfile()
}