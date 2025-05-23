package com.example.cryptos.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.cryptos.ui.scaffold.environment.CoinsGraph
import com.example.cryptos.ui.scaffold.environment.CoinsGraph.CoinDetailsRoute
import com.example.cryptos.ui.scaffold.environment.FavoriteGraph
import com.example.cryptos.ui.scaffold.environment.ProfileGraph
import com.example.cryptos.ui.screens.LocalNavController
import com.example.cryptos.ui.screens.coindetails.CoinDetailsScreen
import com.example.cryptos.ui.screens.favorite.FavoriteCoinsScreen
import com.example.cryptos.ui.screens.home.HomeScreen
import com.example.cryptos.ui.screens.profile.ProfileScreen
import com.example.cryptos.ui.theme.CryptosTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptosTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavApp()
                }
            }
        }
    }
}

@Composable
fun NavApp() {
    val navController = rememberNavController()
    CompositionLocalProvider(
        LocalNavController provides navController
    ) {
        NavHost(
            navController = navController,
            startDestination = CoinsGraph,
            modifier = Modifier.fillMaxSize()
        ) {
            navigation<CoinsGraph>(startDestination = CoinsGraph.CoinsRoute) {
                composable<CoinsGraph.CoinsRoute> { HomeScreen() }
                composable<CoinDetailsRoute> { entry ->
                    CoinDetailsScreen()
                }
            }

            navigation<FavoriteGraph>(startDestination = FavoriteGraph.FavoriteCoinsRoute) {
                composable<FavoriteGraph.FavoriteCoinsRoute> { FavoriteCoinsScreen() }
                composable<FavoriteGraph.FavoriteCoinDetailsRoute> { entry ->
                    CoinDetailsScreen()
                }
            }
            navigation<ProfileGraph>(startDestination = ProfileGraph.ProfileRoute) {
                composable<ProfileGraph.ProfileRoute> { ProfileScreen() }
            }
        }
    }
}