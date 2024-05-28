package com.jaemin.assignment.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaemin.assignment.data.UnsplashRepository
import com.jaemin.assignment.data.model.UnsplashPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val unsplashRepository: UnsplashRepository
) : ViewModel() {

    val favoritePhotos: StateFlow<Collection<UnsplashPhoto>> = unsplashRepository.favoritePhoto
        .stateIn(viewModelScope, SharingStarted.Lazily, emptySet())

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
}