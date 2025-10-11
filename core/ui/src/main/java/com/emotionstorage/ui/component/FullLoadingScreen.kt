package com.emotionstorage.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun FullLoadingScreen(modifier: Modifier = Modifier) {
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
        ){
            // todo: add loading ui
        }
    }
}

@Preview
@Composable
private fun FullLoadingScreenPreview() {
    MooiTheme {
        FullLoadingScreen()
    }
}
