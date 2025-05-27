package com.example.cryptos.ui.scaffold.environment

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.collections.immutable.ImmutableList

@Composable
fun AppNavigationBar(
    navController: NavController,
    tabs: ImmutableList<AppTab>,
    onHeightAppNavBarMeasured: (Dp) -> Unit,
) {
    val density = LocalDensity.current

    NavigationBar(
//        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                val heightPx = coordinates.size.height
                val heightDp = with(density) { heightPx.toDp() }
                onHeightAppNavBarMeasured(heightDp)
            }
    ) {
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val closestNavGraphDestination = currentBackStackEntry?.destination?.hierarchy?.first {
            it is NavGraph
        }
        val closestNavGraphClass = closestNavGraphDestination.routClass()
        val currentTab = tabs.firstOrNull { it.graph::class == closestNavGraphClass }

        tabs.forEach { tab ->
            NavigationBarItem(
                selected = currentTab == tab,
                onClick = {
                    if (currentTab != null) {
                        navController.navigate(tab.graph) {
                            popUpTo(currentTab.graph) {
                                inclusive = true
                                saveState = true
                            }
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(imageVector = tab.icon, contentDescription = null)
                },
                label = {
                    Text(text = stringResource(id = tab.labelRes))
                },
//                colors = NavigationBarItemDefaults.colors(
//                    indicatorColor = MaterialTheme.colorScheme.tertiaryContainer,
//                    selectedIconColor = MaterialTheme.colorScheme.onTertiaryContainer
//                )
            )
        }
    }
}