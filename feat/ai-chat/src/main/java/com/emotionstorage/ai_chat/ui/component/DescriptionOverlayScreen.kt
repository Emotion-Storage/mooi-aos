package com.emotionstorage.ai_chat.ui.component

import SpeechBubble
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.emotionstorage.ai_chat.R
import com.emotionstorage.ui.component.SpeechBubbleTopCenter
import com.emotionstorage.ui.component.SpeechBubbleTopLeft
import com.emotionstorage.ui.theme.MooiTheme
import kotlin.math.roundToInt

enum class HighlightType { PROGRESS_BAR, INPUT_BOX, TOPBAR }

@Composable
fun DescriptionOverlayScreen(
    isVisible: Boolean = true,
    progressBarBounds: Rect = Rect.Zero,
    inputBoxBounds: Rect = Rect.Zero,
    topbarBounds: Rect = Rect.Zero,
    onCheckboxChecked: (Boolean) -> Unit = {},
    onComplete: () -> Unit = {},
) {
    if (!isVisible) return

    var currentStep by remember { mutableIntStateOf(0) }
    var isCheckboxChecked by remember { mutableStateOf(false) }

    val steps =
        listOf(
            HighlightType.PROGRESS_BAR to progressBarBounds,
            HighlightType.INPUT_BOX to inputBoxBounds,
            HighlightType.TOPBAR to topbarBounds,
        )

    val (type, area) = steps[currentStep]

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                ) {
                    if (currentStep < steps.lastIndex) currentStep++ else onComplete()
                },
    ) {
        Canvas(
            Modifier
                .fillMaxSize()
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen),
        ) {
            // 뒷 배경 어둡게
            drawRect(Color.Black.copy(alpha = 0.70f), size = size)

            when (type) {
                HighlightType.PROGRESS_BAR, HighlightType.INPUT_BOX -> {
                    if (area != Rect.Zero) {
                        drawRoundRect(
                            color = Color.Transparent,
                            topLeft = area.topLeft,
                            size = area.size,
                            blendMode = BlendMode.Clear,
                        )
                    }
                }

                HighlightType.TOPBAR -> {
                    if (area != Rect.Zero) {
                        val backButtonRadius = with(density) { 22.dp.toPx() }
                        val backButtonCenterX = area.left + with(density) { 22.dp.toPx() }
                        val backButtonCenterY = area.center.y

                        drawCircle(
                            color = Color.Transparent,
                            radius = backButtonRadius,
                            center = androidx.compose.ui.geometry.Offset(
                                backButtonCenterX,
                                backButtonCenterY
                            ),
                            blendMode = BlendMode.Clear
                        )
                    }
                }
            }
        }

        PositionedBubble(type = type, area = area)

        DescriptionCoachScreen(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
            checked = isCheckboxChecked,
        ) {
            isCheckboxChecked = it
            onCheckboxChecked(it)
        }
    }
}

@Composable
private fun BoxScope.PositionedBubble(
    type: HighlightType,
    area: Rect,
) {
    val density = LocalDensity.current
    val gap = with(density) { 22.dp.toPx() }

    when (type) {
        HighlightType.PROGRESS_BAR -> {
            val y = (area.bottom + gap).roundToInt()
            SpeechBubbleTopCenter(
                text = stringResource(R.string.ai_chat_desc0),
                modifier =
                    Modifier
                        .align(Alignment.TopCenter)
                        .size(265.dp, 101.dp)
                        .offset { IntOffset(0, y) },
            )
        }

        HighlightType.INPUT_BOX -> {
            val bw = with(density) { 265.dp.roundToPx() }
            val bh = with(density) { 84.dp.roundToPx() }

            val x = (area.center.x - bw / 2f).roundToInt()
            val y = (area.top - bh - gap).roundToInt()

            SpeechBubble(
                text = stringResource(R.string.ai_chat_desc1),
                modifier =
                    Modifier
                        .size(265.dp, 84.dp)
                        .offset { IntOffset(x, y) },
            )
        }

        HighlightType.TOPBAR -> {
            val x = with(density) { area.left.toDp() + 8.dp }
            val y = with(density) { area.bottom.toDp() + 8.dp }
            SpeechBubbleTopLeft(
                text = stringResource(R.string.ai_chat_desc2),
                modifier =
                    Modifier
                        .size(222.dp, 122.dp)
                        .offset(x = x, y = y),
            )
        }
    }
}


@Preview
@Composable
fun DescriptionOverlayScreenPreview() {
    MooiTheme {
        DescriptionOverlayScreen()
    }
}
