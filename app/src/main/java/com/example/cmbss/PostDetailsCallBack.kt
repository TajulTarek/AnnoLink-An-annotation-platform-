package com.example.cmbss

interface PostDetailsCallBack {
    fun OnBackArrow()
    fun OnApply(posterId:String,isAlreadyApplied:Boolean)
}