package com.emotionstorage.my.ui.keyDescription.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun WhenToUseKeyDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            tonalElevation = 6.dp,
            shadowElevation = 12.dp,
            color = MooiTheme.colorScheme.backgroundDefault,
            modifier = Modifier.width(328.dp),
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 17.dp, start = 20.dp, end = 20.dp, bottom = 35.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(28.dp),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.graphic_key),
                            contentDescription = "열쇠",
                            tint = MooiTheme.colorScheme.primaryBlue500,
                            modifier =
                                Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center),
                        )
                        IconButton(
                            onClick = onDismiss,
                            modifier =
                                Modifier
                                    .align(Alignment.CenterEnd)
                                    .offset(18.dp, 0.dp),
                        ) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(R.drawable.ic_close),
                                contentDescription = "닫기",
                                tint = Color.White,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(5.dp))

                    Text(
                        text = "열쇠는 이럴 때 쓰면 좋아요!",
                        style = MooiTheme.typography.head2.copy(lineHeight = 30.sp),
                        color = MooiTheme.colorScheme.primaryBlue500,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(Modifier.size(19.dp))

                    // TODO : 이모지 확인 필요
                    TipItem(
                        emoji = "\uD83D\uDE22",
                        title = "불안하고 지쳐, ",
                        boldTitle = "위로가 필요할 때",
                        reverse = false,
                        content = "감정이 벅차오를 땐, 과거의 나 또는\n모이의 말이 위로가 될 수 있어요.",
                    )

                    Spacer(modifier = Modifier.size(15.dp))

                    TipItem(
                        emoji = "\uD83E\uDD14",
                        title = "호기심 폭발! ",
                        boldTitle = "지금 당장 알고 싶을 때",
                        reverse = false,
                        content = "어떤 감정을 적었는지 너무 궁금해서,\n하루도 기다리기 힘든 순간이 있죠.",
                    )

                    Spacer(modifier = Modifier.size(15.dp))

                    TipItem(
                        emoji = "\uD83D\uDC91",
                        title = "이 달라졌을 때",
                        boldTitle = "소중한 사람과의 감정",
                        reverse = true,
                        content = "상황이 바뀌면, 과거의 내가 남긴\n감정을 다시 꺼내보고 싶어져요.",
                    )

                    Spacer(modifier = Modifier.size(15.dp))

                    TipItem(
                        emoji = "\uD83D\uDD51",
                        title = "를 비교하고 싶을 때",
                        boldTitle = "지금의 나와 과거의 나",
                        reverse = true,
                        content = "지금의 나는 그때와 어떻게 달라졌고,\n얼마나 성장했을까요?",
                    )
                }
            }
        }
    }
}

@Composable
private fun TipItem(
    emoji: String,
    title: String,
    boldTitle: String,
    reverse: Boolean,
    content: String,
) {
    Row(
        modifier = Modifier.padding(horizontal = 5.dp),
    ) {
        Text(
            text = emoji,
            modifier = Modifier.padding(end = 6.dp),
        )

        Column(
            modifier = Modifier.weight(1f),
        ) {
            if (reverse) {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style =
                                MooiTheme.typography.body4.toSpanStyle().copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                ),
                            {
                                append(boldTitle)
                            },
                        )
                        withStyle(
                            style =
                                MooiTheme.typography.body5.toSpanStyle().copy(
                                    color = Color.White,
                                ),
                            {
                                append(title)
                            },
                        )
                    },
                )
            } else {
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style =
                                MooiTheme.typography.body5.toSpanStyle().copy(
                                    color = Color.White,
                                ),
                            {
                                append(title)
                            },
                        )

                        withStyle(
                            style =
                                MooiTheme.typography.body4.toSpanStyle().copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                ),
                            {
                                append(boldTitle)
                            },
                        )
                    },
                )
            }

            Spacer(modifier = Modifier.size(5.dp))

            Text(
                text = content,
                style = MooiTheme.typography.caption4.copy(lineHeight = 20.sp),
                color = MooiTheme.colorScheme.gray300,
            )
        }
    }
}

@Preview
@Composable
fun TipItemPreview() {
    MooiTheme {
        TipItem(
            emoji = "😀",
            title = "제목",
            boldTitle = "입니다",
            reverse = false,
            content = "내용",
        )
    }
}

@Preview
@Composable
fun WhenToUseKeyDialogPreview() {
    MooiTheme {
        WhenToUseKeyDialog(
            onDismiss = {},
        )
    }
}
