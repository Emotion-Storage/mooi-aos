package com.emotionstorage.ui.util

import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder

// navigate to destination with clear stack
fun <T : Any> NavHostController.navigateWithClearStack(destRoute: T) {
    val currentRoute = currentBackStackEntry?.destination?.route

    this.navigate(destRoute) {
        popUpTo(currentRoute ?: destRoute.toString()) {
            inclusive = true
        }
    }
}

fun <T : Any> NavHostController.navigateAsRoot(
    destRoute: T,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    navigate(destRoute) {
        popUpTo(graph.id) {
            inclusive = true
        }
        launchSingleTop = true
        builder()
    }
}
