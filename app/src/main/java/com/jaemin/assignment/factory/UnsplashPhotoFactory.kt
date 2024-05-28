package com.jaemin.assignment.factory

import com.jaemin.assignment.model.UnsplashPhoto
import com.jaemin.assignment.model.UnsplashPhotoUrls

object UnsplashPhotoFactory {

    private fun createUnsplashPhoto(id: String): UnsplashPhoto {
        return UnsplashPhoto(
            id = id,
            urls = UnsplashPhotoUrls(FAKE_LINK)
        )
    }

    fun createUnsplashPhotos(count: Int): List<UnsplashPhoto> {
        return (1..count).map { id ->
            createUnsplashPhoto(id.toString())
        }
    }

    private const val FAKE_LINK = "https://images.unsplash.com/photo-1715954582482-82f25cc96e78?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MTYzNjV8MHwxfHJhbmRvbXx8fHx8fHx8fDE3MTY3OTE2OTR8&ixlib=rb-4.0.3&q=80&w=400"
}