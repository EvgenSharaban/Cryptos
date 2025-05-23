package com.example.cryptos.ui.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import com.example.cryptos.ui.scaffold.environment.AppNavigationBar
import com.example.cryptos.ui.scaffold.environment.AppToolbar
import com.example.cryptos.ui.scaffold.environment.NavTabs
import com.example.cryptos.ui.screens.LocalNavController

@Composable
fun AppScaffold(
    title: String,
    showNavigationUp: Boolean,
    toolbarAction: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    val navController = LocalNavController.current
    Scaffold(
        topBar = {
            AppToolbar(
                title = title,
                showNavigationUp = showNavigationUp,
                onAction = {
                    toolbarAction?.invoke()
                }
            )
        },
        bottomBar = {
            AppNavigationBar(navController = navController, tabs = NavTabs)
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}