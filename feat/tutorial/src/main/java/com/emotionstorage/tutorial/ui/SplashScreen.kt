package com.emotionstorage.tutorial.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.tutorial.presentation.SplashAction
import com.emotionstorage.tutorial.presentation.SplashSideEffect
import com.emotionstorage.tutorial.presentation.SplashViewModel
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
    navToTutorial: () -> Unit = {},
    navToHome: () -> Unit = {},
) {
    LaunchedEffect(Unit) {
        viewModel.onAction(SplashAction.Initiate)

        viewModel.container.sideEffectFlow.collect { effect ->
            when (effect) {
                is SplashSideEffect.AutoLoginFailed -> navToTutorial()
                is SplashSideEffect.AutoLoginSuccess -> navToHome()
            }
        }
    }

    StatelessSplashScreen(modifier = modifier)
}

@Composable
private fun StatelessSplashScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background),
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(padding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(148.dp)
                        .offset(y = 214.dp)
                        .background(Color.Black),
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    MooiTheme {
        StatelessSplashScreen(modifier = Modifier.fillMaxSize())
    }
}
