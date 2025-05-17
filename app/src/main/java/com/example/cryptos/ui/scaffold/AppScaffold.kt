package com.example.cryptos.ui.scaffold

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.cryptos.ui.scaffold.environment.AppNavigationBar
import com.example.cryptos.ui.scaffold.environment.AppToolbar
import com.example.cryptos.ui.scaffold.environment.NavTabs
import com.example.cryptos.ui.screens.LocalNavController

@Composable
fun AppScaffold(
    @StringRes titleRes: Int,
    showNavigationUp: Boolean,
    content: @Composable (PaddingValues) -> Unit,
) {
    val navController = LocalNavController.current
    Scaffold(
        topBar = {
            AppToolbar(
                titleRes = titleRes,
                showNavigationUp = showNavigationUp
            )
        },
        bottomBar = {
            AppNavigationBar(navController = navController, tabs = NavTabs)
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}