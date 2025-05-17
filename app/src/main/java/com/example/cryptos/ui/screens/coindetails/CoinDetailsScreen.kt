package com.example.cryptos.ui.screens.coindetails

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
fun CoinDetailsScreen(id: String) {
    AppScaffold(
        titleRes = R.string.coin_details_screen,
        showNavigationUp = true,
    ) { paddingValues ->
        Text(
            text = "Mocked details screen with id = $id",
            textAlign = TextAlign.Center,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight()
                .padding(paddingValues),
        )
    }
}