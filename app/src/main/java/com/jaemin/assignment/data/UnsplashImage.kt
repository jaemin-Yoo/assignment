package com.jaemin.assignment.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashImage(
    @SerialName("id") val id: String,
    @SerialName("urls") val urls: UnsplashImageUrls,
)