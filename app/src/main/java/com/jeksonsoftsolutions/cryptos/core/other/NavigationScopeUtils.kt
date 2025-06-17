package com.jeksonsoftsolutions.cryptos.core.other

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.jeksonsoftsolutions.cryptos.ui.screens.LocalNavAnimatedContentScope

@Composable
inline fun WithNavAnimatedScope(
    scope: AnimatedContentScope,
    crossinline content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalNavAnimatedContentScope provides scope) {
        content()
    }
}