package com.jaemin.assignment.api

import com.jaemin.assignment.BuildConfig
import com.jaemin.assignment.data.model.UnsplashSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashService {

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("client_id") clientId: String = BuildConfig.UNSPLASH_ACCESS_KEY
    ): UnsplashSearchResponse
}