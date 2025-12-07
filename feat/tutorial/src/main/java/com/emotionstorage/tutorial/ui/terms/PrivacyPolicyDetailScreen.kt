package com.emotionstorage.tutorial.ui.terms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.content.PrivacyPolicyContent
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun PrivacyPolicyDetailScreen(navToBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                showBackground = false,
                showBackButton = true,
                onBackClick = navToBack,
            )
        },
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding),
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
