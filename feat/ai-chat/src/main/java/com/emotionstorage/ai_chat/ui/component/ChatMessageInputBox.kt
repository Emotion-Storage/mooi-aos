package com.emotionstorage.ai_chat.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun ChatMessageInputBox(
    modifier: Modifier = Modifier,
    text: String,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    sendEnabled: Boolean = true,
    showSendingDisabled: Boolean = false,
    focusRequester: FocusRequester = remember { FocusRequester() },
    onTextChange: (String) -> Unit,
    onSendMessage: () -> Unit = {},
    onFocusChanged: (Boolean) -> Unit = {},
) {
    val interaction = remember { MutableInteractionSource() }
    val canSend = text.isNotBlank() && enabled && !readOnly && sendEnabled
    val shape = RoundedCornerShape(100.dp)

    val sendButtonEnabled = canSend && !showSendingDisabled

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Color(0xFF26262C), shape)
                .border(1.dp, MooiTheme.colorScheme.gray800, shape)
                .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .heightIn(min = 46.dp, max = 100.dp)
                    .focusRequester(focusRequester)
                    .background(color = Color.Transparent, shape)
                    .onFocusChanged { onFocusChanged(it.isFocused) },
            value = text,
            onValueChange = { if (enabled && !readOnly) onTextChange(it) },
            singleLine = false,
            maxLines = 6,
            readOnly = readOnly,
            enabled = enabled,
            textStyle = MooiTheme.typography.caption3.copy(color = Color.White),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { if (canSend) onSendMessage() }),
            interactionSource = interaction,
            cursorBrush = SolidColor(MooiTheme.colorScheme.primaryBlue500),
            decorationBox = { inner ->
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 19.dp, top = 11.dp, bottom = 11.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (text.isEmpty()) {
                        Text(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.Center),
                            text = "지금 떠오르는 감정을 적어보세요",
                            style =
                                MooiTheme.typography.caption3.copy(
                                    color = MooiTheme.colorScheme.gray600,
                                ),
                        )
                    }
                    inner()
                }
            },
        )

        if (canSend || readOnly || showSendingDisabled) {
            Box(
                modifier =
                    Modifier
                        .padding(end = 7.dp, bottom = 7.dp, top = 7.dp)
                        .size(33.dp)
                        .clip(CircleShape)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            enabled = sendButtonEnabled,
                            onClick = onSendMessage,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                val iconColorFilter =
                    if (showSendingDisabled) {
                        ColorFilter.tint(MooiTheme.colorScheme.gray600)
                    } else {
                        null
                    }

                Image(
                    painter = painterResource(R.drawable.graphic_send),
                    contentDescription = if (showSendingDisabled) "전송 불가" else "전송",
                    modifier =
                        Modifier
                            .size(33.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = iconColorFilter,
                )
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
        var text by remember { mutableStateOf("테스트\n안녕하세요") }
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
