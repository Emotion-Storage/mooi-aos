package com.emotionstorage.ai_chat.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ai_chat.ui.component.ChatMessageInputBox
import com.emotionstorage.ai_chat.ui.component.ChatMessageList
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun ChatScreen(
    roomId: String,
    modifier: Modifier = Modifier,
    navToTimeCapsuleDetail: (capsuleId: String) -> Unit = {}
) {
    Scaffold(
        modifier = modifier
            .fillMaxWidth()
            .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(onBackClick = {
                // todo: show exit chat modal
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MooiTheme.colorScheme.background)
                .padding(innerPadding)
                .imePadding()
        ) {
            ChatMessageList(
                modifier = Modifier.weight(1f),
            )
            ChatMessageInputBox(
                modifier = Modifier.fillMaxWidth(),
                onSendMessage = {
                    // todo: handle send message
                }
            )
        }
    }
}

@Preview
@Composable
private fun ChatScreenPreview() {
    ChatScreen("roomId")
}
