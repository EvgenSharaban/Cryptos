package com.jeksonsoftsolutions.cryptos.ui.scaffold.environment

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import com.jeksonsoftsolutions.cryptos.R
import kotlinx.collections.immutable.persistentListOf

data class AppTab(
    val icon: ImageVector,
    @StringRes val labelRes: Int,
    val graph: Any,
)

val NavTabs = persistentListOf(
    AppTab(
        icon = Icons.AutoMirrored.Default.List,
        labelRes = R.string.coins_screen,
        graph = CoinsGraph,
    ),
    AppTab(
        icon = Icons.Default.Favorite,
        labelRes = R.string.favorite_coins_screen,
        graph = FavoriteGraph,
    ),
    AppTab(
        icon = Icons.Default.AccountCircle,
        labelRes = R.string.profile_screen,
        graph = ProfileGraph,
    )
)