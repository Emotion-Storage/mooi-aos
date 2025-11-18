package com.emotionstorage.ui.component.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

@Composable
fun LoadingOverlay(
    modifier: Modifier = Modifier,
    delayDuration: Long = 1500L,
) {
    var showOverlay by remember { mutableStateOf(false) }
    LaunchedEffect("init") {
        delay(delayDuration)
        showOverlay = true
    }

    if (showOverlay) {
        Box(
            modifier =
                modifier
                    .fillMaxSize()
                    .zIndex(20f)
                    .background(color = Color.Black.copy(alpha = 0.7f)),
            contentAlignment = Alignment.Center,
        ) {
            LoadingDots(
                dotSize = 13.dp,
                dotSpacing = 10.dp,
            )
        }
    }
}
