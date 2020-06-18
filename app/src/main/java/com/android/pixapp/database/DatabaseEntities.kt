package com.android.pixapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */

/**
 * DatabasePicture represents a video entity in the database.
 */
@Entity
data class DatabasePicture constructor(
        @PrimaryKey
        val url: String,
        val uploader: String,
        val likes: Int,
        val views: Int,
        val downloads: Int,
        val tags: String)


@Entity
data class DatabaseUser constructor(
        @PrimaryKey
        val username: String,
        val password: String,
        val age: Int)