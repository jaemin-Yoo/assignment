package com.jaemin.assignment.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.jaemin.assignment.data.UnsplashRepository
import com.jaemin.assignment.model.UnsplashPhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val unsplashRepository: UnsplashRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _feedUiState: MutableStateFlow<FeedUiState> = MutableStateFlow(FeedUiState.Nothing)
    val feedUiState = _feedUiState.asStateFlow()

    val favoritePhotos: StateFlow<Collection<UnsplashPhoto>> = unsplashRepository.favoritePhoto
        .stateIn(viewModelScope, SharingStarted.Lazily, emptySet())

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun search() {
        _feedUiState.value = FeedUiState.Loading
        viewModelScope.launch {
            delay(300)
            unsplashRepository.getUnsplashPhotosStream(searchQuery.value)
                .cachedIn(viewModelScope)
                .catch {
                    _feedUiState.value = FeedUiState.Error
                }
                .collectLatest { pagingData ->
                    _feedUiState.value = FeedUiState.Success(pagingData)
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

    sealed interface FeedUiState {
        data object Nothing : FeedUiState

        data object Loading : FeedUiState

        data object Error : FeedUiState

        data class Success(
            val unsplashPhotos: PagingData<UnsplashPhoto>
        ) : FeedUiState
    }
}