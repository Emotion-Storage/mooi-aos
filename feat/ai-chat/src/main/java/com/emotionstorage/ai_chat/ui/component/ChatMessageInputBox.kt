package com.emotionstorage.ai_chat.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.TextInput
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun ChatMessageInputBox(
    modifier: Modifier = Modifier,
    onSendMessage: (String) -> Unit = {}
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val (message, setMessage) = remember { mutableStateOf("") }

    Row(
        modifier
            .fillMaxWidth()
            .background(MooiTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextInput(
            modifier = Modifier.weight(1f),
            value = message,
            onValueChange = setMessage,
            placeHolder = "메시지를 입력하세요",
            showCharCount = false
        )
        Button(
            onClick = {
                onSendMessage(message)
                setMessage("")
//                keyboardController?.hide()
//                focusManager.clearFocus()
            }
        ) {
            Text("전송")
        }
    }
}

@Preview
@Composable
private fun ChatInputPreview() {
    ChatMessageInputBox()
}
