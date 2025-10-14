package com.emotionstorage.alarm.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun EmptyPushHolder() {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = MooiTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "아직 도착한 알림이 없어요.",
            style = MooiTheme.typography.caption2,
            color = MooiTheme.colorScheme.gray400
        )
    }
}

@Preview
@Composable
fun EmptyPushHolderPreview() {
    MooiTheme {
        EmptyPushHolder()
    }
}
