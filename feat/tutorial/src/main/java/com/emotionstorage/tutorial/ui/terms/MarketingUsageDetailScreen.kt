package com.emotionstorage.tutorial.ui.terms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.emotionstorage.ui.annotation.PreviewScreenRatios
import com.emotionstorage.ui.content.MarketingUsageContent
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun MarketingUsageDetailScreen(navToBack: () -> Unit = {}) {
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
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .padding(innerPadding),
        ) {
            MarketingUsageContent()
        }
    }
}

@PreviewScreenRatios
@Composable
private fun MarketingUsageDetailScreenPreview() {
    MooiTheme {
        MarketingUsageDetailScreen()
    }
}
