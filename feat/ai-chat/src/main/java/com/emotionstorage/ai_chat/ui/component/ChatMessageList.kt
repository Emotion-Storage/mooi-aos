package com.emotionstorage.ai_chat.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ai_chat.domain.model.ChatMessage
import com.emotionstorage.ai_chat.domain.model.ChatMessage.MessageSource
import com.emotionstorage.common.getKorDayOfWeek
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun ChatMessageList(
    modifier: Modifier = Modifier,
    chatMessages: List<ChatMessage> = listOf(),
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        itemsIndexed(items = chatMessages, key = { _, item -> item.id }) { index, item ->
            if (index == 0 || chatMessages[index - 1].timestamp.toLocalDate() != item.timestamp.toLocalDate()) {
                DateDivider(
                    date = item.timestamp.toLocalDate(),
                    modifier = Modifier.padding(vertical = if (index != 0) 16.dp else 0.dp),
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            ChatMessageItem(
                modifier = Modifier.padding(horizontal = 16.dp),
                chatMessage = item,
                showProfile = (
                    index == 0 ||
                        item.source != chatMessages[index - 1].source ||
                        chatMessages[index - 1].timestamp.toLocalDate() != item.timestamp.toLocalDate()
                ),
            )
        }
    }
}

@Composable
private fun DateDivider(
    modifier: Modifier = Modifier,
    date: LocalDate = LocalDate.now(),
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Color.Transparent),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Color.Gray),
        )
        Text(
            text = "${date.year}년 ${date.monthValue}월 ${date.dayOfMonth}일 ${date.getKorDayOfWeek()}",
            style = MooiTheme.typography.body3,
            color = Color.Gray,
        )
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(Color.Gray),
        )
    }
}

@Composable
private fun ChatMessageItem(
    chatMessage: ChatMessage,
    modifier: Modifier = Modifier,
    showProfile: Boolean = false,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = if (chatMessage.source == MessageSource.CLIENT) Alignment.End else Alignment.Start,
    ) {
        if (showProfile) {
            if (chatMessage.source == MessageSource.SERVER) {
                Box(
                    modifier =
                        Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(Color.Gray),
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
        Box(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .widthIn(max = (screenWidth * 0.8).dp)
                    .background(
                        if (chatMessage.source == MessageSource.CLIENT) {
                            MooiTheme.colorScheme.primary
                        } else {
                            MooiTheme.colorScheme.gray500
                        },
                    )
                    .padding(8.dp),
        ) {
            Text(
                text = chatMessage.content,
                style = MooiTheme.typography.body3,
            )
        }
    }
}

@Preview
@Composable
private fun ChatMessageListPreview() {
    val chatMessages =
        List(6, init = { it }).map { it ->
            ChatMessage(
                roomId = "",
                source = if (it % 3 == 0) MessageSource.CLIENT else MessageSource.SERVER,
                content = "안녕하세요",
                timestamp = LocalDateTime.of(2025, 9, 2, 17, it, 1, 1),
            )
        } +
            listOf(
                ChatMessage(
                    roomId = "",
                    source = MessageSource.SERVER,
                    content = "안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요.",
                    timestamp = LocalDateTime.of(2025, 9, 3, 17, 1, 1, 1),
                ),
                ChatMessage(
                    roomId = "",
                    source = MessageSource.CLIENT,
                    content = "안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요.",
                    timestamp = LocalDateTime.of(2025, 9, 3, 17, 1, 1, 1),
                ),
            ) +
            List(6, init = { it }).map { it ->
                ChatMessage(
                    roomId = "",
                    source = if (it % 3 == 0) MessageSource.SERVER else MessageSource.CLIENT,
                    content = "안녕하세요",
                    timestamp = LocalDateTime.of(2025, 9, 4, 17, it, 1, 1),
                )
            }

    MooiTheme {
        ChatMessageList(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MooiTheme.colorScheme.background),
            chatMessages = chatMessages,
        )
    }
}
