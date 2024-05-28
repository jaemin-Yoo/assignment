package com.jaemin.assignment.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaemin.assignment.data.UnsplashRepository
import com.jaemin.assignment.model.UnsplashPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val unsplashRepository: UnsplashRepository
) : ViewModel() {

    private val _favoritesUiState: MutableStateFlow<FavoritesUiState> =
        MutableStateFlow(FavoritesUiState.Empty)
    val favoritesUiState = _favoritesUiState.asStateFlow()

    init {
        viewModelScope.launch {
            unsplashRepository.favoritePhoto
                .stateIn(viewModelScope, SharingStarted.Lazily, emptySet())
                .collectLatest { collection ->
                    val favoritesPhotos = collection.toList()
                    if (favoritesPhotos.isNotEmpty()) {
                        _favoritesUiState.value = FavoritesUiState.Success(favoritesPhotos)
                    } else {
                        _favoritesUiState.value = FavoritesUiState.Empty
                    }
                }
        }
    }

    fun addFavorite(photo: UnsplashPhoto) {
        viewModelScope.launch {
            unsplashRepository.addFavorite(photo.id, photo.urls.small)
        }
    }

    fun removeFavorite(photo: UnsplashPhoto) {
        viewModelScope.launch {
            unsplashRepository.removeFavorite(photo.id, photo.urls.small)
        }
    }

    sealed interface FavoritesUiState {
        data object Empty : FavoritesUiState

        data object Loading : FavoritesUiState

        data object Error : FavoritesUiState

        data class Success(
            val favoritePhotos: List<UnsplashPhoto>
        ) : FavoritesUiState
    }
}