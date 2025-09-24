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
    onComplete: () -> Unit = {},
) {
    if (!isVisible) return

    var currentStep by remember { mutableIntStateOf(0) }
    var isCheckboxChecked by remember { mutableStateOf(false) }

    val steps = listOf(
        HighlightType.PROGRESS_BAR to progressBarBounds,
        HighlightType.INPUT_BOX to inputBoxBounds,
        HighlightType.TOPBAR to topbarBounds,
    )

    val (type, area) = steps[currentStep]


    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (currentStep < steps.lastIndex) currentStep++ else onComplete()
            }
    ) {

        // 1) 어둡게 + 현재 타깃만 둥글게 뚫기
        Canvas(Modifier.fillMaxSize()) {
            drawRect(Color.Black.copy(alpha = 0.70f), size = size)

            if (area != Rect.Zero) {
                val pad = 6.dp.toPx()
                val r = Rect(
                    area.left - pad,
                    area.top - pad,
                    area.right + pad,
                    area.bottom + pad
                )
                drawRoundRect(
                    color = Color.Transparent,
                    topLeft = r.topLeft,
                    size = r.size,
                    blendMode = BlendMode.Clear
                )
            }
        }

        // 2) 말풍선: 각 타깃 Rect 기준으로 위치 계산
        PositionedBubble(type = type, area = area)

        DescriptionCoachScreen(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            checked = isCheckboxChecked,
        ) {
            isCheckboxChecked = it
        }
    }
}

@Composable
private fun BoxScope.PositionedBubble(
    type: HighlightType,
    area: Rect,
) {
    val d = LocalDensity.current

    when (type) {
        // 1) 진행바: 중앙 하단 (위꼭지)
        HighlightType.PROGRESS_BAR -> {
            val y = with(d) { area.bottom.toDp() + 12.dp }
            SpeechBubbleTopCenter(
                text = stringResource(R.string.ai_chat_desc0),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = y)
            )
        }
        // 2) 입력창: 바로 위 중앙 (아래꼭지)
        HighlightType.INPUT_BOX -> {
            val bubbleW = with(d) { 265.dp.roundToPx() }
            val bubbleH = with(d) { 84.dp.roundToPx() }
            val x = (area.center.x - bubbleW / 2f).roundToInt()
            val y = (area.top - bubbleH - with(d) { 12.dp.toPx() }).roundToInt()
            SpeechBubble(
                text = stringResource(R.string.ai_chat_desc1),
                modifier = Modifier.offset { IntOffset(x, y) }
            )
        }
        // 3) 상단바(뒤로가기 영역): 바 바로 아래 좌측 (좌상단꼬리)
        HighlightType.TOPBAR -> {
            val x = with(d) { area.left.toDp() + 8.dp }
            val y = with(d) { area.bottom.toDp() + 8.dp }
            SpeechBubbleTopLeft(
                text = stringResource(R.string.ai_chat_desc2),
                modifier = Modifier.offset(x = x, y = y)
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