package com.jeksonsoftsolutions.cryptos.ui.components

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.Transformation
import com.jeksonsoftsolutions.cryptos.R

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RoundImageCoinAvatar(
    logo: String,
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope
) {
    with(sharedTransitionScope) {
        Card(
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(0.dp),
            modifier = modifier.sharedElement(
                sharedTransitionScope.rememberSharedContentState(key = "coin_avatar_$logo"),
                animatedVisibilityScope = animatedContentScope
            )
        ) {
            val logoUrl = String.format(LOGO_URL_FORMATTER, logo.lowercase())

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(logoUrl)
                    .transformations(TrimBottomPaddingTransformation())
                    .build(),
                placeholder = painterResource(R.drawable.case_detail_sample),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxSize()
            )
        }
    }
}

private class TrimBottomPaddingTransformation : Transformation {
    override val cacheKey: String
        get() = "TrimPaddingTransformation"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val width = input.width
        val height = input.height
        val trimTop = 2 // Number of pixels to crop from top
        val trimBottom = 5 // Number of pixels to crop from the bottom

        val newHeight = (height - trimTop - trimBottom).coerceAtLeast(1)

        return Bitmap.createBitmap(input, 0, trimTop, width, newHeight)
    }
}

private const val LOGO_URL_FORMATTER = "https://assets.coincap.io/assets/icons/%s@2x.png"