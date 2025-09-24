package com.emotionstorage.ai_chat.ui.component

import SpeechBubble
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ai_chat.R
import com.emotionstorage.ui.component.SpeechBubbleTopCenter
import com.emotionstorage.ui.component.SpeechBubbleTopLeft
import com.emotionstorage.ui.theme.MooiTheme

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
    // Constant 처리 고민해보기
    val totalSteps = 3

    val tutorialSteps = listOf(
        DescriptionStep(
            highlightAreas = listOf(progressBarBounds),
            highlightType = HighlightType.PROGRESS_BAR,
            speechBubbleComponent = {
                SpeechBubbleTopCenter(
                    text = stringResource(R.string.ai_chat_desc0)
                )
            }
        ),

        DescriptionStep(
            highlightAreas = listOf(inputBoxBounds),
            highlightType = HighlightType.INPUT_BOX,
            speechBubbleComponent = {
                SpeechBubble(
                    text = stringResource(R.string.ai_chat_desc1)
                )
            }
        ),

        DescriptionStep(
            highlightAreas = listOf(topbarBounds),
            highlightType = HighlightType.TOPBAR,
            speechBubbleComponent = {
                SpeechBubbleTopLeft(
                    text = stringResource(R.string.ai_chat_desc2)
                )
            }
        ),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (currentStep < totalSteps - 1) {
                    currentStep++
                } else {
                    onComplete()
                }
            }
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            drawRect(
                color = Color.Black.copy(alpha = 0.7f),
                size = size
            )

            tutorialSteps[currentStep].highlightAreas.forEach { area ->
                if (area != Rect.Zero) {
                    drawRect(
                        color = Color.Transparent,
                        topLeft = area.topLeft,
                        size = area.size,
                        blendMode = BlendMode.Clear
                    )
                }
            }
        }

        // 각 말풍선
        tutorialSteps[currentStep].speechBubbleComponent

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

data class DescriptionStep(
    val highlightAreas: List<Rect>,
    val highlightType: HighlightType,
    val speechBubbleComponent: @Composable BoxScope.() -> Unit
)

enum class HighlightType {
    PROGRESS_BAR,
    INPUT_BOX,
    TOPBAR
}

@Preview
@Composable
fun DescriptionOverlayScreenPreview() {
    MooiTheme {
        DescriptionOverlayScreen()
    }
}