package com.emotionstorage.tutorial.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.tutorial.presentation.SplashViewModel
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    splashViewModel: SplashViewModel = hiltViewModel(),
    navToTutorial: () -> Unit = {},
    navToLogin: () -> Unit = {},
    navToHome: () -> Unit = {},
) {
    // todo: nav to home on splash done & automatic login success
    // todo: nav to login/tutorial on splash done & automatic login fail

    Scaffold(
        modifier = modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize()
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            // todo: change to app logo
            Box(modifier = Modifier
                .size(148.dp)
                .background(Color.Black)
                .align(Alignment.Center))
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    MooiTheme {
        SplashScreen()
    }
}