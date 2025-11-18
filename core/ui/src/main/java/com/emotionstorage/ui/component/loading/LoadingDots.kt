package com.emotionstorage.ui.component.loading

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun LoadingDots(
    modifier: Modifier = Modifier,
    dotSize: Dp = 5.dp,
    dotSpacing: Dp = 5.dp,
) {
    val transition = rememberInfiniteTransition(label = "typing")
    val a1 by transition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(500), RepeatMode.Reverse),
        label = "1",
    )
    val a2 by transition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(500, delayMillis = 150), RepeatMode.Reverse),
        label = "2",
    )
    val a3 by transition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(500, delayMillis = 300), RepeatMode.Reverse),
        label = "3",
    )

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dotSpacing),
    ) {
        repeat(3) { idx ->
            val alpha =
                when (idx) {
                    0 -> a1
                    1 -> a2
                    else -> a3
                }
            Box(
                Modifier
                    .size(dotSize)
                    .clip(CircleShape)
                    .background(Color(0xFFD9D9D9).copy(alpha = alpha)),
            )
        }
    }
}

@Preview
@Composable
private fun LodaingDotsPreview() {
    MooiTheme {
        LoadingDots()
    }
}
