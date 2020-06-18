package com.android.pixapp.domain

data class PixAppPicture(val url: String,
                         val uploader: String,
                         val likes: Int,
                         val views: Int,
                         val downloads: Int,
                         val tags: String)

data class PixAppUser(val username: String,
                      val password: String,
                      val age: Int)