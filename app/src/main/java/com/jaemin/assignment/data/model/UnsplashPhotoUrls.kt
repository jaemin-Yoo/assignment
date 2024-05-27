package com.jaemin.assignment.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashPhotoUrls(
    @SerialName("small") val small: String
)