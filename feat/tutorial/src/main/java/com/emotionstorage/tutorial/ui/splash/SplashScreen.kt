package com.emotionstorage.tutorial.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
    viewModel: SplashViewModel = hiltViewModel(),
    navToLogin: () -> Unit = {},
    navToHome: () -> Unit = {},
) {

    val state = viewModel.state.collectAsState()
    StatelessSplashScreen(
        state = state.value,
        modifier = modifier,
        navToLogin = navToLogin,
        navToHome = navToHome,
    )
}

@Composable
private fun StatelessSplashScreen(
    state: SplashViewModel.State,
    modifier: Modifier = Modifier,
    navToLogin: () -> Unit = {},
    navToHome: () -> Unit = {},
) {
    LaunchedEffect(state) {
        if (state.splashState != SplashViewModel.State.SplashState.Done) return@LaunchedEffect

        when (state.autoLoginState) {
            SplashViewModel.State.AutoLoginState.Loading -> {
                // do nothing
            }
            SplashViewModel.State.AutoLoginState.Success -> {
                navToHome()
            }

            SplashViewModel.State.AutoLoginState.Fail -> {
                navToLogin()
            }
        }
    }

    Scaffold(
        modifier = modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize()
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            // todo: change to app logo
            Box(
                modifier = Modifier
                    .size(148.dp)
                    .background(Color.Black)
                    .align(Alignment.Center)
            )
        }
    }
}


@Preview
@Composable
private fun SplashScreenPreview() {
    MooiTheme {
        StatelessSplashScreen(
            state = SplashViewModel.State(),
        )
    }
}