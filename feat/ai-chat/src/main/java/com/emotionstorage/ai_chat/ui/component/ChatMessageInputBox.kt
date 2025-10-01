package com.emotionstorage.ai_chat.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun ChatMessageInputBox(
    modifier: Modifier = Modifier,
    text: String,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    focusRequester: FocusRequester = remember { FocusRequester() },
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit = {},
    onFocusChanged: (Boolean) -> Unit = {},
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
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(78.dp)
                    .padding(16.dp),
        ) {
            // 입력 캡슐
            BasicTextField(
                value = text,
                onValueChange = { if (enabled && !readOnly) onTextChange(it) },
                singleLine = true,
                maxLines = 1,
                readOnly = readOnly,
                enabled = enabled,
                textStyle = MooiTheme.typography.caption3.copy(lineHeight = 24.sp, color = Color.White),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { if (canSend) onSendMessage() }),
                interactionSource = interaction,
                decorationBox = { inner ->
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .background(MooiTheme.colorScheme.gray800, shape)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .padding(start = 6.dp),
                        ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = "지금 떠오르는 감정을 적어보세요",
                                    style =
                                        MooiTheme.typography.caption3.copy(
                                            lineHeight = 24.sp,
                                            color = MooiTheme.colorScheme.gray600,
                                        ),
                                )
                            }
                            inner()
                        }
                    }
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .focusRequester(focusRequester)
                        .onFocusChanged { onFocusChanged(it.isFocused) },
            )

            if (canSend || readOnly) {
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 7.dp)
                            .size(33.dp)
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                enabled = canSend,
                                onClick = onSendMessage,
                            ),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painterResource(R.drawable.send),
                        contentDescription = "전송",
                        modifier = Modifier.size(33.dp),
                        contentScale = ContentScale.Fit,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChatInputEmptyPreview() {
    MooiTheme {
        var text by remember { mutableStateOf("") }
        ChatMessageInputBox(
            text = text,
            onTextChange = { text = it },
            onSendMessage = { text = "" },
        )
    }
}

@Preview
@Composable
private fun ChatInputPreview() {
    MooiTheme {
        var text by remember { mutableStateOf("테스트") }
        ChatMessageInputBox(
            text = text,
            onTextChange = { text = it },
            onSendMessage = {
                println("Send clicked!")
                text = ""
            },
        )
    }
}
