package com.emotionstorage.ai_chat.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun ChatMessageInputBox(
    modifier: Modifier = Modifier,
    text: String,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit = {},
) {
    val interaction = remember { MutableInteractionSource() }
    val canSend = text.isNotBlank() && enabled && !readOnly
    val shape = RoundedCornerShape(100.dp)

    Surface(
        modifier = modifier.fillMaxWidth(),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        color = MooiTheme.colorScheme.background,
    ) {
        // 바 높이 78dp
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(78.dp)
                .padding(16.dp)
        ) {
            // 입력 캡슐
            BasicTextField(
                value = text,
                onValueChange = { if (enabled && !readOnly) onTextChange(it) },
                singleLine = true,
                maxLines = 1,
                readOnly = readOnly,
                enabled = enabled,
                textStyle = MooiTheme.typography.caption3.copy(
                    lineHeight = 24.sp,
                    color = Color.White
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { if (canSend) onSendMessage() }),
                interactionSource = interaction,
                // 여기서 '캡슐 배경 + placeholder + trailing 아이콘'을 한 번에 그림
                decorationBox = { inner ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(MooiTheme.colorScheme.gray800, shape)
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // 텍스트 영역
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 6.dp) // 왼쪽 여유
                        ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = "지금 떠오르는 감정을 적어보세요",
                                    style = MooiTheme.typography.caption3.copy(
                                        lineHeight = 24.sp,
                                        color = MooiTheme.colorScheme.gray600
                                    )
                                )
                            }
                            inner() // 실제 텍스트 입력
                        }

                        // trailing send icon (캡슐 안쪽, 오른쪽 끝)
                        AnimatedVisibility(
                            visible = canSend || readOnly,
                        ) {
                            IconButton(
                                onClick = onSendMessage,
                                modifier = Modifier
                                    .size(33.dp)
                                    .background(
                                        color = MooiTheme.colorScheme.primary,
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    painter = painterResource(com.emotionstorage.ui.R.drawable.send),
                                    contentDescription = "전송",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }
    }
}

@Preview
@Composable
private fun ChatInputPreview() {
    MooiTheme {
        var text by remember { mutableStateOf("") }
        ChatMessageInputBox(
            text = text,
            onTextChange = { text = it },
            onSendMessage = { text = "" },
        )

    }
}
