package com.android.pixapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.pixapp.domain.PixAppPicture
import com.android.pixapp.domain.PixAppUser

/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */

/**
 * DatabasePicture represents a picture entity in the database.
 */
@Entity
data class DatabasePicture constructor(
        @PrimaryKey
        val id: Int,
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

/**
 * DatabaseUser represents a user entity in the database.
 */
@Entity
data class DatabaseUser constructor(
        @PrimaryKey
        val email: String,
        val password: String,
        val age: Int)


/**
 * Map DatabasePictures to domain entities
 */
fun List<DatabasePicture>.asDomainModel(): List<PixAppPicture> {
    return map {
        PixAppPicture(
            id = it.id,
            url = it.url,
            uploader = it.uploader,
            likes = it.likes,
            views = it.views,
            downloads = it.downloads,
            tags = it.tags,
            previewURL = it.previewURL,
            imageSize = it.imageSize,
            type = it.type,
            comments = it.comments,
            favorites = it.favorites)
    }
}


fun DatabaseUser.asDomainModel(): PixAppUser{
   return PixAppUser(email, password, age)
}