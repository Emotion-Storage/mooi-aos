package com.emotionstorage.ai_chat.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.emotionstorage.ai_chat.ui.component.ChatMessageInputBox
import com.emotionstorage.ai_chat.ui.component.ChatProgressBar
import com.emotionstorage.ai_chat.ui.component.DescriptionOverlay
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.appBar.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun AIChatDescriptionScreen(
    roomId: Long,
    modifier: Modifier = Modifier,
    onCheckboxChanged: (Boolean) -> Unit = {},
    onStartChat: (Long) -> Unit = {},
) {
    var progressRect by remember { mutableStateOf(Rect.Zero) }
    var inputRect by remember { mutableStateOf(Rect.Zero) }
    var topbarRect by remember { mutableStateOf(Rect.Zero) }
    var showDescription by rememberSaveable { mutableStateOf(true) }

    StatelessAIChatDescriptionScreen(
        modifier = modifier,
        progress = 0.03f,
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
        contentWindowInsets =
            WindowInsets.safeDrawing.only(
                WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom,
            ),
    ) { innerPadding ->
        if (showDescription) {
            DescriptionOverlay(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .zIndex(10f),
                progressBarBounds = progressBarBounds,
                inputBoxBounds = inputBoxBounds,
                topbarBounds = topbarBounds,
                onCheckboxChecked = onCheckboxChanged,
                onComplete = onDescriptionCompleted,
            )
        }

        Column(
            modifier =
                modifier
                    .fillMaxWidth()
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .padding(innerPadding)
                    .imePadding(),
        ) {
            TopAppBar(
                modifier =
                    Modifier.onGloballyPositioned {
                        onTopbarRect(it.boundsInParent())
                    },
                showBackButton = true,
                onBackClick = {},
            )

            ChatProgressBar(
                progress = progress,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned {
                            onProgressRect(it.boundsInParent())
                        },
            )

            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .offset(y = 15.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.graphic_ai_chat),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(255.dp)
                            .alpha(0.2f)
                            .blur(5.dp),
                )
            }

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(78.dp)
                        .onGloballyPositioned { onInputBoxRect(it.boundsInParent()) },
            ) {
                // 실제 InputBox는 밴드 안에서 padding 주고 배치
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                ) {
                    ChatMessageInputBox(
                        text = "",
                        modifier = Modifier.fillMaxWidth(),
                        enabled = false,
                        readOnly = true,
                        onTextChange = {},
                        onSendMessage = {},
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AIChatDescriptionScreenPreview() {
    MooiTheme {
        StatelessAIChatDescriptionScreen()
    }
}
