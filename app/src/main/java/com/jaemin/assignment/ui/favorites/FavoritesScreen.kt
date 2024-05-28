package com.jaemin.assignment.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jaemin.assignment.R
import com.jaemin.assignment.model.UnsplashPhoto
import com.jaemin.assignment.model.UnsplashPhotoUrls
import com.jaemin.assignment.ui.photo.UnsplashPhotoListScreen
import com.jaemin.assignment.ui.theme.AssignmentTheme
import com.jaemin.assignment.ui.favorites.FavoritesViewModel.FavoritesUiState

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favoritesUiState by viewModel.favoritesUiState.collectAsStateWithLifecycle()
    FavoritesScreen(
        favoritesUiState = favoritesUiState,
        onAddFavorite = viewModel::addFavorite,
        onRemoveFavorite = viewModel::removeFavorite
    )
}

@Composable
fun FavoritesScreen(
    favoritesUiState: FavoritesUiState,
    onAddFavorite: (UnsplashPhoto) -> Unit = {},
    onRemoveFavorite: (UnsplashPhoto) -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        when (favoritesUiState) {
            FavoritesUiState.Empty -> Text(text = "즐겨찾기 한 이미지가 없습니다.")
            FavoritesUiState.Loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            is FavoritesUiState.Success -> {
                val favoritePhotos = favoritesUiState.favoritePhotos
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
            FavoritesUiState.Error -> Unit
        }
    }
}

@Preview
@Composable
fun FavoritesScreenPreview() {
    AssignmentTheme {
        FavoritesScreen(
            favoritesUiState = FavoritesUiState.Success(
                listOf(
                    UnsplashPhoto(
                        id = "1",
                        urls = UnsplashPhotoUrls("https://images.unsplash.com/photo-1715954582482-82f25cc96e78?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MTYzNjV8MHwxfHJhbmRvbXx8fHx8fHx8fDE3MTY3OTE2OTR8&ixlib=rb-4.0.3&q=80&w=400")
                    )
                )
            )
        )
    }
}