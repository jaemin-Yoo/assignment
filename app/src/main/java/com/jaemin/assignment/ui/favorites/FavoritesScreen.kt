package com.jaemin.assignment.ui.favorites

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.jaemin.assignment.model.UnsplashPhoto
import com.jaemin.assignment.model.UnsplashPhotoUrls
import com.jaemin.assignment.ui.photo.UnsplashPhotoListScreen

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favoritePhotos by viewModel.favoritePhotos.collectAsState()
    FavoritesScreen(
        favoritePhotos = favoritePhotos.toList(),
        onAddFavorite = viewModel::addFavorite,
        onRemoveFavorite = viewModel::removeFavorite
    )
}

@Composable
fun FavoritesScreen(
    favoritePhotos: List<UnsplashPhoto> = emptyList(),
    onAddFavorite: (UnsplashPhoto) -> Unit = {},
    onRemoveFavorite: (UnsplashPhoto) -> Unit = {}
) {
    UnsplashPhotoListScreen(
        unsplashPhotos = favoritePhotos,
        favoritePhotos = favoritePhotos
    ) { id, isLiked ->
        if (isLiked) {
            onAddFavorite(id)
        } else {
            onRemoveFavorite(id)
        }
    }
}

@Preview
@Composable
fun FavoritesScreenPreview() {
    FavoritesScreen(
        favoritePhotos = listOf(
            UnsplashPhoto(
                id = "1",
                urls = UnsplashPhotoUrls("https://images.unsplash.com/photo-1715954582482-82f25cc96e78?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MTYzNjV8MHwxfHJhbmRvbXx8fHx8fHx8fDE3MTY3OTE2OTR8&ixlib=rb-4.0.3&q=80&w=400")
            )
        )
    )
}