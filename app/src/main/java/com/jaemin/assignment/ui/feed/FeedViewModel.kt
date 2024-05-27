package com.jaemin.assignment.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jaemin.assignment.data.UnsplashRepository
import com.jaemin.assignment.data.model.UnsplashPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val unsplashRepository: UnsplashRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _unsplashPhotosStream = MutableStateFlow<Flow<PagingData<UnsplashPhoto>>?>(null)
    val unsplashPhotosStream: StateFlow<Flow<PagingData<UnsplashPhoto>>?> = _unsplashPhotosStream

    val favoritePhotos: StateFlow<Collection<UnsplashPhoto>> = unsplashRepository.favoritePhoto
        .stateIn(viewModelScope, SharingStarted.Lazily, emptySet())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun search() {
        viewModelScope.launch {
            _unsplashPhotosStream.value = unsplashRepository.getUnsplashPhotosStream(searchQuery.value).cachedIn(viewModelScope)
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
}