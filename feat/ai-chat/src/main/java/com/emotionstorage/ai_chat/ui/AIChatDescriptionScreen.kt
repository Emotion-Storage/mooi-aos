package com.emotionstorage.ai_chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ai_chat.ui.component.ChatMessageInputBox
import com.emotionstorage.ai_chat.ui.component.ChatProgressBar
import com.emotionstorage.ai_chat.ui.component.DescriptionOverlayScreen
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun AIChatDescriptionScreen(
    roomId: String,
    modifier: Modifier = Modifier,
    onCheckboxChanged: (Boolean) -> Unit = {},
    onStartChat: (String) -> Unit = {},
) {
    var progressRect by remember { mutableStateOf(Rect.Zero) }
    var inputRect by remember { mutableStateOf(Rect.Zero) }
    var topbarRect by remember { mutableStateOf(Rect.Zero) }
    var showDescription by rememberSaveable { mutableStateOf(true) }

    StatelessAIChatDescriptionScreen(
        modifier = modifier,
        progress = 0.1f,
        showDescription = showDescription,
        progressBarBounds = progressRect,
        inputBoxBounds = inputRect,
        topbarBounds = topbarRect,
        onProgressRect = { progressRect = it },
        onInputBoxRect = { inputRect = it },
        onTopbarRect = { topbarRect = it },
        onCheckboxChanged = onCheckboxChanged,
        onDescriptionCompleted = {
            showDescription = false
            onStartChat(roomId)
        },
    )
}

@Composable
private fun StatelessAIChatDescriptionScreen(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    showDescription: Boolean = true,
    progressBarBounds: Rect = Rect.Zero,
    inputBoxBounds: Rect = Rect.Zero,
    topbarBounds: Rect = Rect.Zero,
    onCheckboxChanged: (Boolean) -> Unit = {},
    onDescriptionCompleted: () -> Unit = {},
    onProgressRect: (Rect) -> Unit = {},
    onInputBoxRect: (Rect) -> Unit = {},
    onTopbarRect: (Rect) -> Unit = {},
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MooiTheme.colorScheme.background)
                .padding(innerPadding)
                .imePadding()
        ) {
            // TopAppbar 을 UI 컴포넌트로
            TopAppBar(
                modifier = Modifier.onGloballyPositioned {
                    onTopbarRect(it.boundsInParent())
                },
                showBackButton = true,
                onBackClick = {})

            ChatProgressBar(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    // ProgressBar 위치 추적
                    .onGloballyPositioned {
                        onProgressRect(it.boundsInParent())
                    }
            )

            Box(
                modifier = Modifier.weight(1f)
            )

            ChatMessageInputBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned {
                        onInputBoxRect(it.boundsInParent())
                    },
                onSendMessage = {}
            )
        }
    }

    if (showDescription) {
        DescriptionOverlayScreen(
            progressBarBounds = progressBarBounds,
            inputBoxBounds = inputBoxBounds,
            topbarBounds = topbarBounds,
            onCheckboxChecked = onCheckboxChanged,
            onComplete = onDescriptionCompleted
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AIChatDescriptionScreenPreview() {
    MooiTheme {
        StatelessAIChatDescriptionScreen()
    }
}