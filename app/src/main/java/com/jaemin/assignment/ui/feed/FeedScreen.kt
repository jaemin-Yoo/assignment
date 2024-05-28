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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.jaemin.assignment.R
import com.jaemin.assignment.model.UnsplashPhoto
import com.jaemin.assignment.model.UnsplashPhotoUrls
import com.jaemin.assignment.ui.feed.FeedViewModel.FeedUiState
import com.jaemin.assignment.ui.photo.UnsplashPhotoListScreen
import com.jaemin.assignment.ui.theme.AssignmentTheme
import kotlinx.coroutines.flow.flow

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel()
) {
    val feedUiState by viewModel.feedUiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val favoritePhotos by viewModel.favoritePhotos.collectAsState()
    FeedScreen(
        feedUiState = feedUiState,
        searchQuery = searchQuery,
        favoritePhotos = favoritePhotos.toList(),
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSearch = viewModel::search,
        onAddFavorite = viewModel::addFavorite,
        onRemoveFavorite = viewModel::removeFavorite
    )
}

@Composable
fun FeedScreen(
    feedUiState: FeedUiState,
    searchQuery: String,
    favoritePhotos: List<UnsplashPhoto> = emptyList(),
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
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() }
        ),
        maxLines = 1,
        singleLine = true,
    )
}

@Preview
@Composable
fun FeedScreenPreview(
    @PreviewParameter(FeedScreenPreviewParamProvider::class) unsplashPhotoStream: PagingData<UnsplashPhoto>
) {
    AssignmentTheme {
        FeedScreen(
            feedUiState = FeedUiState.Success(unsplashPhotoStream),
            searchQuery = "",
            favoritePhotos = listOf(
                UnsplashPhoto(
                    id = "1",
                    urls = UnsplashPhotoUrls("https://images.unsplash.com/photo-1715954582482-82f25cc96e78?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MTYzNjV8MHwxfHJhbmRvbXx8fHx8fHx8fDE3MTY3OTE2OTR8&ixlib=rb-4.0.3&q=80&w=400")
                )
            )
        )
    }
}

private class FeedScreenPreviewParamProvider :
    PreviewParameterProvider<PagingData<UnsplashPhoto>> {

    override val values: Sequence<PagingData<UnsplashPhoto>> =
        sequenceOf(
            PagingData.from(
                listOf(
                    UnsplashPhoto(
                        id = "1",
                        urls = UnsplashPhotoUrls("https://images.unsplash.com/photo-1715954582482-82f25cc96e78?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MTYzNjV8MHwxfHJhbmRvbXx8fHx8fHx8fDE3MTY3OTE2OTR8&ixlib=rb-4.0.3&q=80&w=400")
                    ),
                    UnsplashPhoto(
                        id = "2",
                        urls = UnsplashPhotoUrls("https://images.unsplash.com/photo-1715954582482-82f25cc96e78?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MTYzNjV8MHwxfHJhbmRvbXx8fHx8fHx8fDE3MTY3OTE2OTR8&ixlib=rb-4.0.3&q=80&w=400")
                    )
                )
            )
        )
}