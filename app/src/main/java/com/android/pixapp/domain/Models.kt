package com.android.pixapp.domain

import com.android.pixapp.database.DatabaseUser

data class PixAppPicture(val id: Int,
                         val url: String,
                         val uploader: String,
                         val likes: Int,
                         val views: Int,
                         val downloads: Int,
                         val tags: String,
                         val previewURL: String,
                         val imageSize: Int,
                         val type: String,
                         val comments: Int,
                         val favorites: Int)

data class PixAppUser(val email: String,
                      val password: String,
                      val age: Int)


fun PixAppUser.asDatabaseModel(): DatabaseUser{
    return DatabaseUser(email,password,age)
}