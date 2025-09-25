package com.emotionstorage.ai_chat.ui.component

import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun ChatProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
) {
    val animatedProgress =
        animateFloatAsState(
            targetValue = progress,
            animationSpec =
                tween(
                    durationMillis = 500,
                    easing = EaseOut,
                ),
            label = "chat_progress",
        )

    Box(
        modifier =
            modifier
                .height(5.dp)
                .background(MooiTheme.colorScheme.gray800),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(animatedProgress.value)
                    .fillMaxHeight()
                    .background(
                        MooiTheme.colorScheme.secondary,
                    ),
        )
    }
}

@Preview
@Composable
fun ChatProgressBarPreview() {
    MooiTheme {
        ChatProgressBar(progress = 0.3f)
    }
}
