package com.jaemin.assignment.ui.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.jaemin.assignment.R
import com.jaemin.assignment.data.model.UnsplashPhoto
import com.jaemin.assignment.data.model.UnsplashPhotoUrls
import com.jaemin.assignment.ui.photo.UnsplashPhotoListScreen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val unsplashPhotosStream by viewModel.unsplashPhotosStream.collectAsState()
    val favoritePhotos by viewModel.favoritePhotos.collectAsState()
    FeedScreen(
        searchQuery = searchQuery,
        unsplashPhotoStream = unsplashPhotosStream,
        favoritePhotos = favoritePhotos.toList(),
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSearch = viewModel::search,
        onAddFavorite = viewModel::addFavorite,
        onRemoveFavorite = viewModel::removeFavorite
    )
}

@Composable
fun FeedScreen(
    searchQuery: String,
    unsplashPhotoStream: Flow<PagingData<UnsplashPhoto>>?,
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
        unsplashPhotoStream?.let { dataFlow ->
            val pagingItems = dataFlow.collectAsLazyPagingItems()
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
    @PreviewParameter(FeedScreenPreviewParamProvider::class) unsplashPhotoStream: Flow<PagingData<UnsplashPhoto>>
) {
    FeedScreen(
        searchQuery = "나무",
        unsplashPhotoStream = unsplashPhotoStream,
        favoritePhotos = listOf(
            UnsplashPhoto(
                id = "1",
                urls = UnsplashPhotoUrls("https://images.unsplash.com/photo-1715954582482-82f25cc96e78?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MTYzNjV8MHwxfHJhbmRvbXx8fHx8fHx8fDE3MTY3OTE2OTR8&ixlib=rb-4.0.3&q=80&w=400")
            )
        )
    )
}

private class FeedScreenPreviewParamProvider :
    PreviewParameterProvider<Flow<PagingData<UnsplashPhoto>>> {

    override val values: Sequence<Flow<PagingData<UnsplashPhoto>>> =
        sequenceOf(
            flowOf(
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
            ),
        )
}