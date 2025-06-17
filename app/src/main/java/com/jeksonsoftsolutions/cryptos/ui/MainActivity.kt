package com.jeksonsoftsolutions.cryptos.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.jeksonsoftsolutions.cryptos.ui.scaffold.environment.CoinsGraph
import com.jeksonsoftsolutions.cryptos.ui.scaffold.environment.CoinsGraph.CoinDetailsRoute
import com.jeksonsoftsolutions.cryptos.ui.scaffold.environment.FavoriteGraph
import com.jeksonsoftsolutions.cryptos.ui.scaffold.environment.ProfileGraph
import com.jeksonsoftsolutions.cryptos.ui.screens.LocalNavAnimatedContentScope
import com.jeksonsoftsolutions.cryptos.ui.screens.LocalNavController
import com.jeksonsoftsolutions.cryptos.ui.screens.LocalSharedTransitionScope
import com.jeksonsoftsolutions.cryptos.ui.screens.coindetails.CoinDetailsScreen
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.favorite.FavoriteCoinsScreen
import com.jeksonsoftsolutions.cryptos.ui.screens.coins_lists.home.HomeScreen
import com.jeksonsoftsolutions.cryptos.ui.screens.profile.edit_profile.EditProfileScreen
import com.jeksonsoftsolutions.cryptos.ui.screens.profile.profile.ProfileScreen
import com.jeksonsoftsolutions.cryptos.ui.theme.CryptosTheme
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavApp() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        SharedTransitionLayout {
            CompositionLocalProvider(LocalSharedTransitionScope provides this) {
                NavHost(
                    navController = navController,
                    startDestination = CoinsGraph,
                    modifier = Modifier.fillMaxSize()
                ) {
                    navigation<CoinsGraph>(startDestination = CoinsGraph.CoinsRoute) {
                        composable<CoinsGraph.CoinsRoute> {
                            CompositionLocalProvider(LocalNavAnimatedContentScope provides this) {
                                HomeScreen()
                            }
                        }
                        composable<CoinDetailsRoute> { entry ->
                            CompositionLocalProvider(LocalNavAnimatedContentScope provides this) {
                                CoinDetailsScreen()
                            }
                        }
                    }

                    navigation<FavoriteGraph>(startDestination = FavoriteGraph.FavoriteCoinsRoute) {
                        composable<FavoriteGraph.FavoriteCoinsRoute> {
                            CompositionLocalProvider(LocalNavAnimatedContentScope provides this) {
                                FavoriteCoinsScreen()
                            }
                        }
                        composable<FavoriteGraph.FavoriteCoinDetailsRoute> { entry ->
                            CompositionLocalProvider(LocalNavAnimatedContentScope provides this) {
                                CoinDetailsScreen()
                            }
                        }
                    }
                    navigation<ProfileGraph>(startDestination = ProfileGraph.ProfileRoute) {
                        composable<ProfileGraph.ProfileRoute> { ProfileScreen() }
                        composable<ProfileGraph.EditProfileRoute> { EditProfileScreen() }
                    }
                }
            }
        }
    }
}