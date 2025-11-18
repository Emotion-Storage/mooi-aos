package com.emotionstorage.ui.component.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun LoadingOverlay(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .zIndex(20f)
            .background(color = Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        LoadingDots()
    }
}
