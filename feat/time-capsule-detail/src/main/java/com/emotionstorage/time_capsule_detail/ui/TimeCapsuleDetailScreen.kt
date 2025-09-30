package com.emotionstorage.time_capsule_detail.ui

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
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
import com.emotionstorage.ui.util.LinearGradient
import com.emotionstorage.ui.util.getIconResId
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private val DUMMY_TIME_CAPSULE =
    TimeCapsule(
        id = "id",
        status = TimeCapsule.STATUS.TEMPORARY,
        title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
        summary =
            "오늘 친구를 만났는데 친구가 지각해놓고 미안하단 말을 하지 않아서 집에 갈 때 기분이 좋지 않았어." +
                "그렇지만 집에서 엄마가 해주신 맛있는 저녁을 먹고 기분이 좋아지더라. " +
                "나를 가장 생각해주는 건 가족밖에 없다는 생각이 들었어.",
        emotions =
            listOf(
                TimeCapsule.Emotion("서운함", icon = 0, 30.0f),
                TimeCapsule.Emotion("고마움", icon = 3, 30.0f),
                TimeCapsule.Emotion("안정감", icon = 4, 80.0f),
            ),
        comments =
            listOf(
                "오늘은 조금 힘든 일이 있었지만, 가족과의 따뜻한 시간 덕분에 긍정적인 감정으로 마무리했어요.",
                "귀가 후 가족애와 안정감을 느끼면서, 부정적 감정을 회복할 수 있었어요.",
                "감정이 복잡하게 얽힌 하루였네요. 하지만 작은 부분에서 감사함을 느끼는 모습이 멋져요.",
            ),
        note =
            "아침엔 기분이 좀 꿀꿀했는데, 가족이랑 저녁 먹으면서 마음이 따뜻하게 풀려버렸다. " +
                "사소한 일에 흔들렸지만 결국 웃으면서 하루를 마무리할 수 있어서 다행이야.",
        logs = emptyList(),
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
    )

@Composable
fun TimeCapsuleDetailScreen(
    id: String,
    modifier: Modifier = Modifier,
    navToBack: () -> Unit = {},
    navToSaveTimeCapsule: (id: String) -> Unit = {}
) {
    // todo: init time capsule deatil screen state

    StatelessTimeCapsuleDetailScreen(
        timeCapsule = DUMMY_TIME_CAPSULE,
        modifier = modifier,
        navToBack = navToBack,
        navToSaveTimeCapsule = navToSaveTimeCapsule
    )
}

@Composable
private fun StatelessTimeCapsuleDetailScreen(
    timeCapsule: TimeCapsule,
    modifier: Modifier = Modifier,
    navToBack: () -> Unit = {},
    navToSaveTimeCapsule: (id: String) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier =
            modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title = timeCapsule.createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm")),
                showBackButton = true,
                onBackClick = navToBack,
                rightComponent = {
                    if (timeCapsule.status == TimeCapsule.STATUS.OPENED) {
                        RoundedToggleButton(
                            isSelected = timeCapsule.isFavorite,
                            onSelect = {
                                // todo: toggle favorite
                            },
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background)
                    .padding(innerPadding)
                    .padding(top = 31.dp, bottom = 55.dp)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState),
        ) {
            TimeCapsuleSummary(
                title = timeCapsule.title,
                summary = timeCapsule.summary,
            )

            DecorativeDots(modifier = Modifier.padding(vertical = 31.dp))

            TimeCapsuleEmotionComments(
                emotions = timeCapsule.emotions,
                comments = timeCapsule.comments,
            )

            // 마음 노트
            TimeCapsuleNote(
                modifier = Modifier.padding(top = 53.dp),
                note = timeCapsule.note,
                onSaveNote = { navToSaveTimeCapsule(timeCapsule.id) }
            )

        }
    }
}

@Composable
private fun TimeCapsuleSummary(
    title: String,
    summary: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = title,
            style = MooiTheme.typography.body1,
            color = Color.White,
        )
        Box(
            modifier =
                Modifier
                    .background(Color(0x1AAECBFA), RoundedCornerShape(15.dp))
                    .padding(18.dp),
        ) {
            Text(
                text = summary,
                style = MooiTheme.typography.caption3.copy(lineHeight = 24.sp),
                color = Color.White,
            )
        }
    }
}

@Composable
private fun DecorativeDots(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        for (alpha in listOf(0.1f, 0.3f, 0.7f)) {
            Box(
                modifier =
                    Modifier
                        .size(8.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    MooiTheme.colorScheme.primary.copy(alpha = alpha),
                                    Color(0xFF9AB4F2).copy(alpha = alpha),
                                    MooiTheme.colorScheme.tertiary.copy(alpha = alpha),
                                ),
                            ),
                            CircleShape,
                        ),
            )
        }
    }
}

@Composable
private fun TimeCapsuleEmotionComments(
    modifier: Modifier = Modifier,
    emotions: List<TimeCapsule.Emotion> = emptyList(),
    comments: List<String> = emptyList(),
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(27.dp)
    ) {
        Text(
            text = "내가 느낀 감정은\n아래와 같이 분석할 수 있어요.",
            style = MooiTheme.typography.body1.copy(lineHeight = 24.sp),
            textAlign = TextAlign.Center,
            color = MooiTheme.colorScheme.primary,
        )
        Emotions(emotions = emotions)
        Comments(comments = comments)
    }
}

@Composable
private fun Emotions(
    modifier: Modifier = Modifier,
    emotions: List<TimeCapsule.Emotion> = emptyList(),
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        for (emotion in emotions) {
            Box(
                modifier =
                    Modifier
                        .height(92.dp)
                        .weight(1f)
                        .background(
                            LinearGradient(
                                listOf(
                                    // alpha = 0.5 * 0.2
                                    Color(0xFF849BEA).copy(alpha = 0.1f),
                                    // alpha = 0.08 * 0.2
                                    Color(0xFF849BEA).copy(alpha = 0.016f),
                                ),
                                angleInDegrees = -18f
                            ),
                            RoundedCornerShape(10.dp),
                        ),
            ) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        if (emotion.getIconResId() == null) {
                            Box(
                                modifier =
                                    Modifier
                                        .size(22.dp)
                                        .background(Color.Gray, CircleShape),
                            )
                        } else {
                            Image(
                                painter = painterResource(id = emotion.getIconResId()!!),
                                modifier = Modifier.size(22.dp),
                                contentDescription = emotion.label,
                            )
                        }
                        Text(
                            text = emotion.label,
                            style = MooiTheme.typography.body8,
                            color = MooiTheme.colorScheme.primary,
                        )
                    }
                    Text(
                        text = "${emotion.percentage?.toInt() ?: "- "}%",
                        style = MooiTheme.typography.head3.copy(lineHeight = 29.sp),
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
    comments: List<String> = emptyList(),
) {
    Column(
        modifier =
            modifier
                .background(Color(0x0AAECBFA), RoundedCornerShape(15.dp))
                .border(1.dp, Color(0x33849BEA), RoundedCornerShape(15.dp))
                .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        for (comment in comments) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(11.dp),
            ) {
                Box(
                    modifier = Modifier.padding(top = 5.dp),
                ) {
                    Box(
                        modifier =
                            Modifier
                                .size(5.dp)
                                .background(Color.White, CircleShape),
                    )
                }
                Text(
                    text = comment,
                    style = MooiTheme.typography.caption3.copy(lineHeight = 22.sp),
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun TimeCapsuleNote(
    modifier: Modifier = Modifier,
    note: String? = null,
    onSaveNote: (note: String) -> Unit = {},
) {
    Column(modifier = modifier.fillMaxWidth()) {
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
            note = note ?: "",
            onSaveNote = onSaveNote
        )
    }
}

@Composable
private fun MindNote(
    modifier: Modifier = Modifier,
    note: String = "",
    onSaveNote: (note: String) -> Unit = {},
) {
    val (noteInput, setNoteInput) = remember { mutableStateOf(note) }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(13.dp),
    ) {
        TextBoxInput(
            modifier =
                Modifier
                    .fillMaxWidth(),
            value = noteInput,
            onValueChange = setNoteInput,
            placeHolder = "지금 내 마음은...",
            showCharCount = true,
            maxCharCount = 1000,
        )

        Box(
            modifier
                .size(140.dp, 46.dp)
                .background(MooiTheme.colorScheme.bottomBarBackground, RoundedCornerShape(10.dp))
                .clickable(
                    onClick = { onSaveNote(noteInput) },
                ),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "변경사항 저장하기",
                style = MooiTheme.typography.body4,
                color = Color.White,
            )
        }
    }
}

// @PreviewScreenSizes
@Preview
@Composable
private fun TimeCapsuleDetailScreenPreview() {
    MooiTheme {
        StatelessTimeCapsuleDetailScreen(
            timeCapsule = DUMMY_TIME_CAPSULE,
        )
    }
}
