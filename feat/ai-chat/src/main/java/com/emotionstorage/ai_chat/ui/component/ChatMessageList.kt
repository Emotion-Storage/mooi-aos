package com.emotionstorage.ai_chat.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.common.toKorDateWithWeekDay
import com.emotionstorage.domain.model.ChatMessage
import com.emotionstorage.domain.model.ChatMessage.MessageSource
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.loading.LoadingDots
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun ChatMessageList(
    modifier: Modifier = Modifier,
    chatMessages: List<ChatMessage> = listOf(),
    listState: LazyListState,
    isMooiTyping: Boolean = false,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize(),
        state = listState,
    ) {
        itemsIndexed(items = chatMessages, key = { _, item -> item.clientId }) { index, item ->
            if (index == 0 || chatMessages[index - 1].timestamp.toLocalDate() != item.timestamp.toLocalDate()) {
                Spacer(modifier = Modifier.height(16.dp))
                DateDivider(
                    date = item.timestamp.toLocalDate(),
                    modifier = Modifier.padding(vertical = if (index != 0) 16.dp else 0.dp),
                )
                Spacer(modifier = Modifier.height(24.dp))
            } else {
                val previousChat = chatMessages[index - 1]
                val topPadding = topPaddingBetween(previousChat, item.source)
                Spacer(Modifier.size(topPadding))
            }

            val showProfile =
                index == 0 ||
                    chatMessages[index - 1].source != item.source ||
                    chatMessages[index - 1].timestamp.toLocalDate() != item.timestamp.toLocalDate()

            ChatMessageItem(
                modifier = Modifier.padding(horizontal = 16.dp),
                chatMessage = item,
                showProfile = showProfile && item.source == MessageSource.SERVER,
            )
        }

        if (isMooiTyping) {
            item {
                val lastMessage = chatMessages.lastOrNull()
                val showTypingProfile =
                    lastMessage == null || lastMessage.source != MessageSource.SERVER

                val topPadding = topPaddingBetween(lastMessage, MessageSource.SERVER)

                Spacer(Modifier.height(topPadding))

                Column(
                    modifier = Modifier.padding(horizontal = 16.dp),
                ) {
                    if (showTypingProfile) {
                        Box(
                            modifier =
                                Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_chat),
                                contentDescription = "mooi",
                            )
                        }
                        Spacer(modifier = Modifier.size(10.dp))
                    }

                    Box(
                        modifier =
                            Modifier
                                .widthIn(max = 64.dp)
                                .heightIn(max = 42.dp)
                                .background(
                                    color = MooiTheme.colorScheme.backgroundTinted,
                                    shape = RoundedCornerShape(20.dp),
                                ),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        LoadingDots(
                            modifier =
                                Modifier.padding(horizontal = 19.5.dp, vertical = 18.5.dp),
                        )
                    }
                }
            }
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
                    .padding(horizontal = 16.dp)
                    .height(1.5.dp)
                    .background(MooiTheme.colorScheme.gray800.copy(alpha = 0.5f)),
        )
        Text(
            text = date.toKorDateWithWeekDay(),
            style = MooiTheme.typography.caption2,
            color = MooiTheme.colorScheme.gray600,
        )
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .height(1.5.dp)
                    .background(MooiTheme.colorScheme.gray800.copy(alpha = 0.5f)),
        )
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
private fun ChatMessageItem(
    chatMessage: ChatMessage,
    modifier: Modifier = Modifier,
    showProfile: Boolean = false,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (chatMessage.source == MessageSource.CLIENT) Alignment.End else Alignment.Start,
    ) {
        if (showProfile) {
            if (chatMessage.source == MessageSource.SERVER) {
                Box(
                    modifier =
                        Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_chat),
                        contentDescription = "mooi",
                    )
                }
            }
            Spacer(modifier = Modifier.size(10.dp))
        }

        if (chatMessage.source == MessageSource.SERVER) {
            Box(
                modifier =
                    Modifier
                        // TODO : 너비 제한을 얼마나 두는게 좋을지 논의 필요
                        .widthIn(max = screenWidth.dp * 0.7f)
                        .heightIn(42.dp)
                        .background(
                            color = MooiTheme.colorScheme.backgroundTinted,
                            shape = RoundedCornerShape(20.dp),
                        ),
                contentAlignment = Alignment.CenterStart,
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                    text = chatMessage.content,
                    style = MooiTheme.typography.caption3,
                    color = Color.White,
                )
            }
        } else {
            Box(
                modifier =
                    Modifier
                        .heightIn(42.dp)
                        .widthIn(max = screenWidth.dp * 0.7f),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    text = chatMessage.content,
                    style = MooiTheme.typography.caption3,
                    color = Color.White,
                )
            }
        }
    }
}

private fun topPaddingBetween(prev: ChatMessage?, curr: MessageSource): androidx.compose.ui.unit.Dp {
    if (prev == null) return 0.dp
    return when {
        prev.source == MessageSource.SERVER && curr == MessageSource.CLIENT -> 20.dp
        prev.source == MessageSource.CLIENT && curr == MessageSource.SERVER -> 10.dp
        prev.source == MessageSource.SERVER && curr == MessageSource.SERVER -> 8.dp
        else -> 20.dp
    }
}

@Preview
@Composable
private fun ChatMessageListPreview() {
    val chatMessages =
        List(6, init = { it }).map { it ->
            ChatMessage(
                roomId = 1L,
                clientId = "1234",
                source = if (it % 3 == 0) MessageSource.CLIENT else MessageSource.SERVER,
                content = "안녕하세요",
                timestamp = LocalDateTime.of(2025, 9, 2, 17, it, 1, 1),
                gaugeScore = 5,
                turnCountScore = 2,
            )
        } +
            listOf(
                ChatMessage(
                    roomId = 1L,
                    clientId = "1234",
                    source = MessageSource.SERVER,
                    content = "안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요.",
                    timestamp = LocalDateTime.of(2025, 9, 3, 17, 1, 1, 1),
                    gaugeScore = 20,
                    turnCountScore = 4,
                ),
                ChatMessage(
                    roomId = 1L,
                    source = MessageSource.CLIENT,
                    content = "안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요. 안녕하세요.",
                    timestamp = LocalDateTime.of(2025, 9, 3, 17, 1, 1, 1),
                    gaugeScore = 40,
                    clientId = "1234",
                    turnCountScore = 6,
                ),
            ) +
            List(6, init = { it }).map { it ->
                ChatMessage(
                    roomId = 1L,
                    source = if (it % 3 == 0) MessageSource.SERVER else MessageSource.CLIENT,
                    clientId = "1234",
                    content = "안녕하세요",
                    timestamp = LocalDateTime.of(2025, 9, 4, 17, it, 1, 1),
                    gaugeScore = 60,
                    turnCountScore = 8,
                )
            }

    MooiTheme {
        ChatMessageList(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MooiTheme.colorScheme.backgroundDefault),
            chatMessages = chatMessages,
            listState = LazyListState(),
        )
    }
}
