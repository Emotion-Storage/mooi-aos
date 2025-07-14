package com.emotionstorage.splash.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SplashScreen(
    navToHome: () -> Unit = {},
    navToLogin: () -> Unit = {},
    navToTutorial: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(modifier) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Splash Screen")
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    SplashScreen()
}