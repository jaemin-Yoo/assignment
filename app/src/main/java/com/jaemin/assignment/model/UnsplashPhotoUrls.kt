package com.jaemin.assignment.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashPhotoUrls(
    @SerialName("small") val small: String
)