package com.emotionstorage.time_capsule_detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.ui.component.RoundedToggleButton
import com.emotionstorage.ui.component.TextBoxInput
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val DUMMY_TIME_CAPSULE = TimeCapsule(
    id = "id",
    status = TimeCapsule.STATUS.TEMPORARY,
    title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
    summary = "오늘 친구를 만났는데 친구가 지각해놓고 미안하단 말을 하지 않아서 집에 갈 때 기분이 좋지 않았어. 그렇지만 집에서 엄마가 해주신 맛있는 저녁을 먹고 기분이 좋아지더라. 나를 가장 생각해주는 건 가족밖에 없다는 생각이 들었어.",
    emotions = listOf(
        TimeCapsule.Emotion("서운함", 30.0f),
        TimeCapsule.Emotion("고마움", 30.0f),
        TimeCapsule.Emotion("안정감", 80.0f)
    ),
    comments = listOf(
        "오늘은 조금 힘든 일이 있었지만, 가족과의 따뜻한 시간 덕분에 긍정적인 감정으로 마무리했어요.",
        "귀가 후 가족애와 안정감을 느끼면서, 부정적 감정을 회복할 수 있었어요.",
        "감정이 복잡하게 얽힌 하루였네요. 하지만 작은 부분에서 감사함을 느끼는 모습이 멋져요."
    ),
    note = "아침엔 기분이 좀 꿀꿀했는데, 가족이랑 저녁 먹으면서 마음이 따뜻하게 풀려버렸다. 사소한 일에 흔들렸지만 결국 웃으면서 하루를 마무리할 수 있어서 다행이야.",
    logs = emptyList(),
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now()
)

@Composable
fun TimeCapsuleDetailScreen(
    id: String, modifier: Modifier = Modifier, navToBack: () -> Unit = {}
) {
    StatelessTimeCapsuleDetailScreen(
        timeCapsule = DUMMY_TIME_CAPSULE, modifier = modifier, navToBack = navToBack
    )
}

@Composable
private fun StatelessTimeCapsuleDetailScreen(
    timeCapsule: TimeCapsule, modifier: Modifier = Modifier, navToBack: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background), topBar = {
            TopAppBar(
                title = timeCapsule.createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm")),
                showBackButton = true,
                onBackClick = navToBack,
                rightComponent = {
                    RoundedToggleButton(
                        isSelected = timeCapsule.isFavorite, onSelect = {
                            // todo: toggle favorite
                        })
                })
        }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp)
                .verticalScroll(scrollState)
        ) {
            // 대화 요약
            Text(
                modifier = Modifier.padding(bottom = 20.dp, top = 21.dp),
                text = timeCapsule.title,
                style = MooiTheme.typography.body1,
                textAlign = TextAlign.Center,
                color = Color.White,
            )
            Box(
                modifier = Modifier
                    .background(Color(0x1AAECBFA), RoundedCornerShape(15.dp))
                    .padding(18.dp)
            ) {
                Text(
                    text = timeCapsule.summary,
                    style = MooiTheme.typography.body4.copy(lineHeight = 24.sp),
                    color = Color.White,
                )
            }

            DecorativeDots(modifier = Modifier.padding(vertical = 31.dp))

            // 감정 분석
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 27.dp),
                text = "내가 느낀 감정은\n아래와 같이 분석할 수 있어요.",
                style = MooiTheme.typography.body1,
                textAlign = TextAlign.Center,
                color = MooiTheme.colorScheme.primary,
            )

            Emotions(
                modifier = Modifier
                    .padding(bottom = 27.dp),
                emotions = timeCapsule.emotions
            )

            Comments(
                modifier = Modifier.padding(bottom = 53.dp),
                comments = timeCapsule.comments
            )

            // 마음 노트
            Text(
                modifier = Modifier.padding(bottom = 5.dp),
                text = "내 마음 노트",
                style = MooiTheme.typography.body1,
                color = Color.White,
            )
            Text(
                modifier = Modifier.padding(bottom = 17.dp),
                text = "타임캡슐에 직접 남기고 싶은 말이 있다면 적어주세요.",
                style = MooiTheme.typography.body5.copy(fontWeight = FontWeight.Light),
                textAlign = TextAlign.Center,
                color = MooiTheme.colorScheme.gray300,
            )
            MindNote(
                modifier = Modifier.padding(bottom = 500.dp),
                note = timeCapsule.note ?: "",
            )
        }
    }
}

@Composable
private fun DecorativeDots(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        for (alpha in listOf(0.1f, 0.3f, 0.7f)) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                MooiTheme.colorScheme.primary.copy(alpha = alpha),
                                Color(0xFF9AB4F2).copy(alpha = alpha),
                                MooiTheme.colorScheme.tertiary.copy(alpha = alpha),
                            )
                        ), CircleShape
                    )
            )
        }

    }
}

@Composable
private fun Emotions(
    modifier: Modifier = Modifier, emotions: List<TimeCapsule.Emotion> = emptyList()
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(11.dp)
    ) {
        for (emotion in emotions) {
            Box(
                modifier = Modifier
                    .height(92.dp)
                    .weight(1f)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0x80849BEA).copy(alpha = 0.1f),
                                Color(0x14849BEA).copy(alpha = 0.016f),
                            )
                        ), RoundedCornerShape(10.dp)
                    )
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        // todo: add emotion emoji
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .background(Color.Gray, CircleShape)
                        )
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = emotion.label,
                            style = MooiTheme.typography.body4.copy(fontSize = 15.sp),
                            color = MooiTheme.colorScheme.primary,
                        )
                    }
                    Text(
                        text = "${emotion.percentage.toInt()}%",
                        style = MooiTheme.typography.head3,
                        color = Color.White,
                    )
                }
            }
        }
    }
}

@Composable
private fun Comments(
    modifier: Modifier = Modifier,
    comments: List<String> = emptyList()
) {
    Column(
        modifier = modifier
            .background(Color(0x0AAECBFA), RoundedCornerShape(15.dp))
            .border(1.dp, Color(0x33849BEA), RoundedCornerShape(15.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        for (comment in comments) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(11.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(Color.White, CircleShape)
                        .offset(y = 8.dp)
                )
                Text(
                    text = comment,
                    style = MooiTheme.typography.body4,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun MindNote(
    modifier: Modifier = Modifier,
    note: String = "",
    onSaveNote: (note: String) -> Unit = {}
) {
    val (noteInput, setNoteInput) = remember { mutableStateOf(note) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(13.dp)
    ) {
        TextBoxInput(
            modifier = Modifier
                .fillMaxWidth(),
            value = noteInput,
            onValueChange = setNoteInput,
            placeHolder = "지금 내 마음은...",
            showCharCount = true,
            maxCharCount = 1000
        )

        Box(
            modifier
                .size(140.dp, 46.dp)
                .background(MooiTheme.colorScheme.bottomBarBackground, RoundedCornerShape(10.dp))
                .clickable(
                    onClick = { onSaveNote(noteInput) }
                )) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "변경사항 저장하기",
                style = MooiTheme.typography.body4,
                color = Color.White
            )
        }
    }
}

//@PreviewScreenSizes
@Preview
@Composable
private fun TimeCapsuleDetailScreenPreview() {
    MooiTheme {
        StatelessTimeCapsuleDetailScreen(
            timeCapsule = DUMMY_TIME_CAPSULE,
        )
    }
}