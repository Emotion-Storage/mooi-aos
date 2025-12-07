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
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier =
            modifier
                .fillMaxSize(),
        containerColor = MooiTheme.colorScheme.backgroundDefault,
    ) { innerPadding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            LoadingDots(
                dotSize = 13.dp,
                dotSpacing = 10.dp,
            )
        }
    }
}

@Preview
@Composable
private fun LoadingScreenPreview() {
    MooiTheme {
        LoadingScreen()
    }
}
