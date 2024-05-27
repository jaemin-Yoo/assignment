package com.jaemin.assignment.ui.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jaemin.assignment.R
import com.jaemin.assignment.data.UnsplashImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun UnsplashImageListItem(
    unsplashImage: UnsplashImage
) {
    Card(
        modifier = Modifier
            .padding(horizontal = dimensionResource(id = R.dimen.card_side_margin))
            .padding(bottom = dimensionResource(id = R.dimen.card_bottom_margin))
    ) {
        Box(Modifier.fillMaxSize()) {
            GlideImage(
                model = unsplashImage.urls.small,
                contentDescription = stringResource(id = R.string.unsplash_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.image_height)),
                contentScale = ContentScale.Crop
            )
            var isLiked by remember { mutableStateOf(false) }
            IconButton(
                onClick = { isLiked = !isLiked },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(dimensionResource(id = R.dimen.margin_small))
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = if (isLiked) "Unlike" else "Like",
                    tint = if (isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}