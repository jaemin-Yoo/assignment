package com.jaemin.assignment.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashPhoto(
    @SerialName("id") val id: String,
    @SerialName("urls") val urls: UnsplashPhotoUrls,
)