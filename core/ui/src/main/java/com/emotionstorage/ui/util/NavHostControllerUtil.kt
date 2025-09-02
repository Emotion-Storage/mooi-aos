package com.emotionstorage.ui.util

import androidx.navigation.NavHostController

// navigate to destination with clear stack
fun <T : Any> NavHostController.navigateWithClearStack(destRoute: T) {
    val currentRoute = currentBackStackEntry?.destination?.route

    this.navigate(destRoute) {
        popUpTo(currentRoute ?: destRoute.toString()) {
            inclusive = true
        }
    }
}