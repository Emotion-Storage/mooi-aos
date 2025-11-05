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
import com.emotionstorage.ui.content.MarketingUsageContent
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun MarketingUsageDetailScreen(navToBack: () -> Unit = {}) {
    Scaffold(
        topBar = {
            TopAppBar(
                showBackground = false,
                fillStatusBar = true,
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
            MarketingUsageContent()
        }
    }
}

@Preview
@Composable
private fun MarketingUsageDetailScreenPreview() {
    MooiTheme {
        MarketingUsageDetailScreen()
    }
}
