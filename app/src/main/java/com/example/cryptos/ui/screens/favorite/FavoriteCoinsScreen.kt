package com.example.cryptos.ui.screens.favorite

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.cryptos.R
import com.example.cryptos.ui.scaffold.AppScaffold

@Composable
fun FavoriteCoinsScreen() {
    AppScaffold(
        titleRes = R.string.favorite_coins_screen,
        showNavigationUp = false,
    ) { paddingValues ->
        Text(
            text = "Mocked Favorite coins screen",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .padding(paddingValues),
        )
    }
}