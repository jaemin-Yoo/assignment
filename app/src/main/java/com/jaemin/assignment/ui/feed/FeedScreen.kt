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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.jaemin.assignment.R
import com.jaemin.assignment.ui.photo.UnsplashPhotoListScreen

@Composable
fun FeedScreen(
    viewModel: FeedViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val unsplashPhotosStream by viewModel.unsplashPhotosStream.collectAsState()

    Column {
        SearchTextField(
            searchQuery = searchQuery,
            onSearchQueryChanged = { query -> viewModel.onSearchQueryChanged(query) },
            onSearch = { viewModel.search() }
        )

        unsplashPhotosStream?.let { pagingDataFlow ->
            val pagingItems = pagingDataFlow.collectAsLazyPagingItems()
            UnsplashPhotoListScreen(unsplashPhotos = pagingItems) { id, isLiked ->
                if (isLiked) {
                    viewModel.addFavorite(id)
                } else {
                    viewModel.removeFavorite(id)
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