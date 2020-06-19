package com.android.pixapp.domain

import android.util.Patterns
import com.android.pixapp.database.DatabaseUser


data class PixAppPicture(val url: String,
                         val uploader: String,
                         val likes: Int,
                         val views: Int,
                         val downloads: Int,
                         val tags: String)

data class PixAppUser(val email: String,
                      val password: String,
                      val age: Int)


fun PixAppUser.asDatabaseModel(): DatabaseUser{
    return DatabaseUser(email,password,age)
}