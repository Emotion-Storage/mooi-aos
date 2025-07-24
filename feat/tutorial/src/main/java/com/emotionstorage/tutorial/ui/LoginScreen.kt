package com.emotionstorage.tutorial.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.tutorial.presentation.LoginViewModel

@Composable
fun LoginRoute(
    navToHome: () -> Unit,
    navToOnboarding: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    LoginScreen(navToHome = navToHome, navToOnboarding = navToOnboarding, modifier = modifier)
}

@Composable
private fun LoginScreen(
    navToHome: () -> Unit,
    navToOnboarding: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text("Login Screen")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(navToHome = {}, navToOnboarding = {})
}