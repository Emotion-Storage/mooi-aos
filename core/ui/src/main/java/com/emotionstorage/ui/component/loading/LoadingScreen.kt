package com.emotionstorage.ui.component.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier =
            modifier
                .fillMaxSize(),
        containerColor = MooiTheme.colorScheme.background,
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            LoadingDots()
        }
    }
}

@Preview
@Composable
private fun FullLoadingScreenPreview() {
    MooiTheme {
        LoadingScreen()
    }
}
