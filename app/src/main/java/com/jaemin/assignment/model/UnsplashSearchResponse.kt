package com.jaemin.assignment.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashSearchResponse(
    @SerialName("results") val results: List<UnsplashPhoto>,
    @SerialName("total_pages") val totalPages: Int
)