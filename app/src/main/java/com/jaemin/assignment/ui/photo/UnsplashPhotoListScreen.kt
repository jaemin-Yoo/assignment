package com.jaemin.assignment.ui.photo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jaemin.assignment.R
import com.jaemin.assignment.factory.UnsplashPhotoFactory
import com.jaemin.assignment.model.UnsplashPhoto
import com.jaemin.assignment.ui.theme.AssignmentTheme

@Composable
fun UnsplashPhotoListScreen(
    unsplashPhotos: List<UnsplashPhoto>,
    favoritePhotos: List<UnsplashPhoto> = emptyList(),
    onClickLikeButton: (UnsplashPhoto, Boolean) -> Unit = { _, _ -> }
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.horizontal_margin),
            vertical = dimensionResource(id = R.dimen.header_margin)
        )
    ) {
        items(
            items = unsplashPhotos,
            key = { it.id }
        ) { photo ->
            UnsplashPhotoListItem(
                unsplashPhoto = photo,
                isFavoritePhoto = favoritePhotos.any { it.id == photo.id },
                onClickLikeButton = onClickLikeButton
            )
        }
    }
}

@Composable
fun UnsplashPhotoListScreen(
    unsplashPhotos: LazyPagingItems<UnsplashPhoto>,
    favoritePhotos: List<UnsplashPhoto> = emptyList(),
    onClickLikeButton: (UnsplashPhoto, Boolean) -> Unit = { _, _ -> }
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.horizontal_margin),
            vertical = dimensionResource(id = R.dimen.header_margin)
        )
    ) {
        items(
            count = unsplashPhotos.itemCount,
            key = { index ->
                val photo = unsplashPhotos[index]
                "${photo?.id ?: ""}${index}"
            }
        ) { index ->
            val photo = unsplashPhotos[index] ?: return@items
            UnsplashPhotoListItem(
                unsplashPhoto = photo,
                isFavoritePhoto = favoritePhotos.any { it.id == photo.id },
                onClickLikeButton = onClickLikeButton
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UnsplashPhotoListItem(
    unsplashPhoto: UnsplashPhoto,
    isFavoritePhoto: Boolean = false,
    onClickLikeButton: (UnsplashPhoto, Boolean) -> Unit = { _, _ -> }
) {
    Card(
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.card_side_margin))
            .padding(bottom = dimensionResource(id = R.dimen.card_bottom_margin))
    ) {
        Box(Modifier.fillMaxSize()) {
            GlideImage(
                model = unsplashPhoto.urls.small,
                contentDescription = stringResource(id = R.string.unsplash_image),
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            var isLiked by remember { mutableStateOf(isFavoritePhoto) }
            IconButton(
                onClick = {
                    isLiked = !isLiked
                    onClickLikeButton(unsplashPhoto, isLiked)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        start = dimensionResource(id = R.dimen.margin_small),
                        bottom = dimensionResource(id = R.dimen.margin_small)
                    )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.favorite_icon_size))
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = CircleShape
                        )
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.Favorite,
                        contentDescription = if (isLiked) "Unlike" else "Like",
                        tint = if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun UnsplashPhotoListItemPreview() {
    AssignmentTheme {
        val unsplashPhoto = UnsplashPhotoFactory.createUnsplashPhoto()
        UnsplashPhotoListItem(unsplashPhoto =  unsplashPhoto)
    }
}

@Preview
@Composable
private fun UnsplashPhotoListScreenPreview() {
    AssignmentTheme {
        val unsplashPhotos = UnsplashPhotoFactory.createUnsplashPhotos(5)
        UnsplashPhotoListScreen(unsplashPhotos = unsplashPhotos)
    }
}