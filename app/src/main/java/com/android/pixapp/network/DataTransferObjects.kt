package com.android.pixapp.network

import com.android.pixapp.database.DatabasePicture
import com.android.pixapp.domain.PixAppPicture
import com.squareup.moshi.JsonClass

/**
 * The following objects are responsible for parsing responses from the server or formatting objects
 * to send to the server. You should convert these to domain objects before using them.
 */

@JsonClass(generateAdapter = true)
data class NetworkPictureContainer(val hits: List<NetworkPicture>)

/**
 *NetworkPicture is the data holding class for the data returned from the API.
 */
@JsonClass(generateAdapter = true)
data class NetworkPicture(
    val id: Int,
    val largeImageURL: String,
    val user: String,
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
 * Convert Network results to domain objects
 */
fun NetworkPictureContainer.asDomainModel(): List<PixAppPicture> {
    return hits.map {
        PixAppPicture(
            id = it.id,
            url = it.largeImageURL,
            uploader = it.user,
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

/**
 * Convert Network results to database objects
 */
fun NetworkPictureContainer.asDatabaseModel(): List<DatabasePicture> {
    return hits.map {
        DatabasePicture(
            id = it.id,
            url = it.largeImageURL,
            uploader = it.user,
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

