package com.example.cryptos.ui.scaffold.environment

import androidx.navigation.NavDestination
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
data object CoinsGraph {

    @Serializable
    data object CoinsRoute

    @Serializable
    data class CoinDetailsRoute(
        val id: String,
    )

}

@Serializable
data object FavoriteGraph {

    @Serializable
    data object FavoriteCoinsRoute

    @Serializable
    data class FavoriteCoinDetailsRoute(
        val id: String,
    )

}

@Serializable
data object ProfileGraph {

    @Serializable
    data object ProfileRoute

}


// ---------------------------------------------------


// this?.destination?.route convert class to String, this function convert String to class
// used reflection this might incorrectly remove or modify code by app optimization, minify, shrinking, obfuscation
fun NavDestination?.routClass(): KClass<*>? {
    return this?.route
        ?.split("/")
        ?.first()
        ?.let { className ->
            generateSequence(className, ::replaceLastDotByDollar)
                .mapNotNull(::tryParseClass)
                .firstOrNull()
        }
}

private fun tryParseClass(className: String): KClass<*>? {
    return runCatching { Class.forName(className).kotlin }.getOrNull()
}

private fun replaceLastDotByDollar(input: String): String? {
    val index = input.lastIndexOf('.')
    return if (index != -1) {
        String(input.toCharArray().apply { set(index, '$') })
    } else {
        null
    }
}