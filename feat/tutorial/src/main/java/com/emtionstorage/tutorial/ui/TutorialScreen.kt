package com.emtionstorage.tutorial.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.theme.MooiTheme
import com.emtionstorage.tutorial.ui.component.PagerWithIndicator

private const val TUTORIAL_PAGE_COUNT = 4

@Composable
fun TutorialScreen(
    modifier: Modifier = Modifier,
    navToLogin: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize()
    ) { innerPadding ->
        PagerWithIndicator(
            modifier = Modifier
                .background(MooiTheme.colorScheme.background)
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 78.dp, bottom = 41.dp),
            pageCount = TUTORIAL_PAGE_COUNT,
            pages = listOf(
                {
                    // 튜토리얼 페이지 1
                    TutorialPage(
                        description = "감정은 나를 돌보는 중요한 신호예요.", title = buildAnnotatedString {
                            append(
                                "감정을 기록하면\n" + "나를 더 잘 "
                            )
                            withStyle(style = SpanStyle(color = MooiTheme.colorScheme.primary)) {
                                append("이해")
                            }
                            append("하고\n")
                            withStyle(style = SpanStyle(color = MooiTheme.colorScheme.primary)) {
                                append("치유")
                            }
                            append("할 수 있어요.")
                        }
                    )
                },
                {
                    // 튜토리얼 페이지 2
                    TutorialPage(
                        description = "어렵게 쓰지 않아도 돼요.",
                        title = buildAnnotatedString {
                            append(
                                "가까운 친구에게 하듯,\n" +
                                        "AI와의 대화를 통해\n"
                            )
                            withStyle(style = SpanStyle(color = MooiTheme.colorScheme.primary)) {
                                append("가볍게")
                            }
                            append(" 털어놓아 보세요.")
                        })
                },
                {
                    // 튜토리얼 페이지 3
                    TutorialPage(
                        description = "며칠 뒤, 타임캡슐을 열어보세요.",
                        title = buildAnnotatedString {
                            append(
                                "시간을 두고 돌아보면,\n" + "당신의 감정을\n"
                            )
                            withStyle(style = SpanStyle(color = MooiTheme.colorScheme.primary)) {
                                append("새롭게")
                            }
                            append(" 마주하게 됩니다.")
                        })
                },
                {
                    // 튜토리얼 페이지 3
                    TutorialPage(
                        description = "변화를 한 눈에, 효과적인 ‘나’ 돌보기",
                        title = buildAnnotatedString {
                            append(
                                "지금, 당신의 감정을 기록하고\n"
                            )
                            withStyle(style = SpanStyle(color = MooiTheme.colorScheme.primary)) {
                                append("더 단단한")
                            }
                            append(" 내가 되어보세요.")
                        },
                        content = {
                            CtaButton(
                                label = "시작하기",
                                onClick = navToLogin,
                                modifier = Modifier.align(Alignment.BottomCenter)
                            )
                        }
                    )
                }
            )
        )
    }
}

/**
 * Tutorial PagerItem
 * @param description 설명
 * @param title 제목
 * @param content 내용
 * @param modifier Modifier
 */
@Composable
private fun ColumnScope.TutorialPage(
    description: String,
    title: AnnotatedString,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier = modifier
//            .background(MooiTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.height(37.dp),
                style = MooiTheme.typography.body1,
                color = MooiTheme.colorScheme.gray500,
                text = description
            )
            Text(
                modifier = Modifier.height(121.dp),
                textAlign = TextAlign.Center,
                style = MooiTheme.typography.head1,
                color = Color.White,
                text = title
            )
        }
        content()
    }
}


@Preview
@Composable
private fun TutorialPreview() {
    MooiTheme {
        TutorialScreen()
    }
}