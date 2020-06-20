package com.android.pixapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// All API services goes here in this file

/**
 * A retrofit service to fetch pixabay pictures
 */
interface PixAppService {
    @GET("api/")
    fun getPicturesAsync(@Query("key") key: String, @Query("per_page") perPage: Int): Deferred<NetworkPictureContainer>
}

/**
 * Main entry point for network access
 */
object PixAppNetwork {

    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pixabay.com/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(HttpClient.init().build())
        .build()

    val pixBay = retrofit.create(PixAppService::class.java)

}

object HttpClient {

    fun init(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()

        httpClient.addInterceptor(logging)
        return httpClient
    }
}


