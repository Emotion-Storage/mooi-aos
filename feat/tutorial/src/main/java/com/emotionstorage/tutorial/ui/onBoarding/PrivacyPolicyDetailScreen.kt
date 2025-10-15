package com.emotionstorage.tutorial.ui.onBoarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.PrivacyPolicyContent
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun PrivacyPolicyDetailScreen(navToBack: () -> Unit = {}) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                showBackground = false,
                showBackButton = true,
                onBackClick = navToBack,
                handleBackPress = true,
                onHandleBackPress = navToBack,
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(WindowInsets.navigationBars)
                    .background(MooiTheme.colorScheme.background),
        ) {
            PrivacyPolicyContent()
        }
    }
}

@Preview
@Composable
private fun PrivacyPolicyDetailScreenPreview() {
    MooiTheme {
        PrivacyPolicyDetailScreen()
    }
}
