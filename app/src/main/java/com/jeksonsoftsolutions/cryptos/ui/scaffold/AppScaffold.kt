package com.jeksonsoftsolutions.cryptos.ui.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.jeksonsoftsolutions.cryptos.ui.scaffold.environment.AppNavigationBar
import com.jeksonsoftsolutions.cryptos.ui.scaffold.environment.AppToolbar
import com.jeksonsoftsolutions.cryptos.ui.scaffold.environment.NavTabs
import com.jeksonsoftsolutions.cryptos.ui.screens.LocalNavController

@Composable
fun AppScaffold(
    title: String,
    showNavigationUp: Boolean,
    onHeightAppNavBarMeasured: (Dp) -> Unit = {},
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
            AppNavigationBar(
                navController = navController,
                tabs = NavTabs,
                onHeightAppNavBarMeasured = onHeightAppNavBarMeasured
            )
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}