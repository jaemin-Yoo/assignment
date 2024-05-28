package com.jaemin.assignment.ui.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.jaemin.assignment.R
import com.jaemin.assignment.factory.UnsplashPhotoFactory
import com.jaemin.assignment.model.UnsplashPhoto
import com.jaemin.assignment.ui.feed.FeedViewModel.FeedUiState
import com.jaemin.assignment.ui.photo.UnsplashPhotoListScreen
import com.jaemin.assignment.ui.theme.AssignmentTheme
import kotlinx.coroutines.flow.flow

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel()
) {
    val feedUiState by viewModel.feedUiState.collectAsState()
    val favoritePhotos by viewModel.favoritePhotos.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    FeedScreen(
        feedUiState = feedUiState,
        favoritePhotos = favoritePhotos.toList(),
        searchQuery = searchQuery,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSearch = viewModel::search,
        onAddFavorite = viewModel::addFavorite,
        onRemoveFavorite = viewModel::removeFavorite
    )
}

@Composable
fun FeedScreen(
    feedUiState: FeedUiState,
    favoritePhotos: List<UnsplashPhoto> = emptyList(),
    searchQuery: String = "",
    onSearchQueryChanged: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    onAddFavorite: (UnsplashPhoto) -> Unit = {},
    onRemoveFavorite: (UnsplashPhoto) -> Unit = {}
) {
    Column {
        SearchTextField(
            searchQuery = searchQuery,
            onSearchQueryChanged = onSearchQueryChanged,
            onSearch = onSearch
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            when (feedUiState) {
                FeedUiState.Nothing -> Text(
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_normal)),
                    text = "이미지를 검색하세요."
                )
                is FeedUiState.Loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                is FeedUiState.Success -> {
                    val pagingItems = remember(feedUiState.unsplashPhotos) {
                        flow {
                            emit(feedUiState.unsplashPhotos)
                        }
                    }.collectAsLazyPagingItems()

                    if (pagingItems.loadState.refresh !is LoadState.Loading && pagingItems.itemCount == 0) {
                        Text(
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.margin_normal)),
                            text = "검색 결과가 존재하지 않습니다."
                        )
                    } else {
                        UnsplashPhotoListScreen(
                            unsplashPhotos = pagingItems,
                            favoritePhotos = favoritePhotos
                        ) { id, isLiked ->
                            if (isLiked) {
                                onAddFavorite(id)
                            } else {
                                onRemoveFavorite(id)
                            }
                        }
                    }
                }
                FeedUiState.Error -> Unit
            }
        }
    }
}

@Composable
fun SearchTextField(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearch: () -> Unit
) {
    OutlinedTextField(
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search)
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = { onSearchQueryChanged("") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close)
                    )
                }
            }
        },
        onValueChange = { onSearchQueryChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.margin_normal)),
        value = searchQuery,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() }
        ),
        maxLines = 1,
        singleLine = true,
    )
}

@Preview
@Composable
fun FeedScreenNothingPreview() {
    AssignmentTheme {
        FeedScreen(feedUiState = FeedUiState.Nothing)
    }
}

@Preview
@Composable
fun FeedScreenSuccessPreview() {
    AssignmentTheme {
        val pagingItems = PagingData.from(
            UnsplashPhotoFactory.createUnsplashPhotos(5)
        )
        val favoritePhotos = UnsplashPhotoFactory.createUnsplashPhotos(3)
        FeedScreen(
            feedUiState = FeedUiState.Success(pagingItems),
            favoritePhotos = favoritePhotos
        )
    }
}