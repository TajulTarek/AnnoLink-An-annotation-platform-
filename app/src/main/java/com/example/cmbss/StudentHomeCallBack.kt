package com.example.cmbss

interface StudentHomeCallBack {
    fun OnAddPost()
    fun OnSignOut()
    fun OnPostClick(id:String,title:String,description:String,deadline:String,salary:String,fullname:String,
                    qualification:String,isAvailable:Boolean)
    fun OnMyPosts()
    fun OnMyProfile()
    fun OnPost(title: String,deadline:String,description:String,salary:String,qualifications:String,time:String)
    fun OnMychannels()
    fun OnOtherProfile(userId:String)
    fun OnChatBox()
}