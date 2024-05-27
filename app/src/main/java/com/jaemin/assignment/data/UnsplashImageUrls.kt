package com.jaemin.assignment.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashImageUrls(
    @SerialName("small") val small: String
)