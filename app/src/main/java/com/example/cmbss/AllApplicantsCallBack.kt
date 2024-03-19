package com.example.cmbss

interface AllApplicantsCallBack {
    fun OnOtherProfileClick(Id:String)
    fun addOnChannel(Id: String,channelId:String)
    fun OnReject(userId: String,channelId:String)
}