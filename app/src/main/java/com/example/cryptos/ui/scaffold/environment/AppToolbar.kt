package com.example.cryptos.ui.scaffold.environment

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.cryptos.ui.screens.LocalNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(
    @StringRes titleRes: Int,
    showNavigationUp: Boolean,
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = stringResource(titleRes))
        },
        navigationIcon = {
            if (showNavigationUp) {
                val navController = LocalNavController.current
                IconButton(
                    onClick = { navController.navigateUp() }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}