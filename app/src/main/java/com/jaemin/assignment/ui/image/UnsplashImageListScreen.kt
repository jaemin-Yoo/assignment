package com.jaemin.assignment.ui.image

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jaemin.assignment.R
import com.jaemin.assignment.data.UnsplashImage
import com.jaemin.assignment.data.UnsplashImageUrls

@Composable
fun UnsplashImageListScreen(
    unsplashImages: List<UnsplashImage>
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.horizontal_margin),
            vertical = dimensionResource(id = R.dimen.header_margin)
        )
    ) {
        items(
            items = unsplashImages,
            key = { it.id }
        ) { image ->
            UnsplashImageListItem(unsplashImage = image)
        }
    }
}

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
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            var isLiked by remember { mutableStateOf(false) }
            IconButton(
                onClick = { isLiked = !isLiked },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        start = dimensionResource(id = R.dimen.margin_small),
                        bottom = dimensionResource(id = R.dimen.margin_small)
                    )
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

@Preview
@Composable
private fun UnsplashImageListScreenPreview(
    @PreviewParameter(UnsplashImageListPreviewParamProvider::class) unsplashImages: List<UnsplashImage>
) {
    UnsplashImageListScreen(unsplashImages = unsplashImages)
}

private class UnsplashImageListPreviewParamProvider :
    PreviewParameterProvider<List<UnsplashImage>> {
    override val values: Sequence<List<UnsplashImage>> =
        sequenceOf(
            emptyList(),
            listOf(
                UnsplashImage(
                    "1",
                    UnsplashImageUrls("https://images.unsplash.com/photo-1715954582482-82f25cc96e78?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MTYzNjV8MHwxfHJhbmRvbXx8fHx8fHx8fDE3MTY3OTE2OTR8&ixlib=rb-4.0.3&q=80&w=400")
                ),
                UnsplashImage(
                    "2",
                    UnsplashImageUrls("https://images.unsplash.com/photo-1715954582482-82f25cc96e78?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MTYzNjV8MHwxfHJhbmRvbXx8fHx8fHx8fDE3MTY3OTE2OTR8&ixlib=rb-4.0.3&q=80&w=400")
                ),
                UnsplashImage(
                    "3",
                    UnsplashImageUrls("https://images.unsplash.com/photo-1715954582482-82f25cc96e78?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3w2MTYzNjV8MHwxfHJhbmRvbXx8fHx8fHx8fDE3MTY3OTE2OTR8&ixlib=rb-4.0.3&q=80&w=400")
                )
            )
        )
}