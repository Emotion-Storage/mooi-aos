package com.emotionstorage.tutorial.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.emotionstorage.tutorial.ui.component.NotificationPermissionAutoRequest
import com.emotionstorage.tutorial.R as tutorialR
import com.emotionstorage.tutorial.ui.component.PagerWithIndicator
import com.emotionstorage.ui.R
import com.emotionstorage.ui.component.button.CtaButton
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.buildHighlightAnnotatedString

private const val TUTORIAL_PAGE_COUNT = 4

@Composable
fun TutorialScreen(
    modifier: Modifier = Modifier,
    navToLogin: () -> Unit = {},
) {
    NotificationPermissionAutoRequest()

    Scaffold(
        modifier =
            modifier
                .background(MooiTheme.colorScheme.backgroundDefault)
                .fillMaxSize()
                .consumeWindowInsets(WindowInsets.systemBars),
    ) { innerPadding ->
        PagerWithIndicator(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(bottom = 41.dp),
            pageCount = TUTORIAL_PAGE_COUNT,
            pageContent =
                { page ->
                    when (page) {
                        0 -> {
                            TutorialPage(
                                description = stringResource(tutorialR.string.tutorial_p0_desc),
                                title = stringResource(tutorialR.string.tutorial_p0_title),
                                titleHighlights =
                                    stringResource(tutorialR.string.tutorial_p0_title_highlights).split(
                                        ',',
                                    ),
                            ) {
                                TutorialGraphicImage(
                                    modifier = Modifier.align(Alignment.Center),
                                    resId = R.drawable.graphic_tutorial_greeting,
                                )
                            }
                        }

                        1 -> {
                            TutorialPage(
                                description = stringResource(tutorialR.string.tutorial_p1_desc),
                                title = stringResource(tutorialR.string.tutorial_p1_title),
                                titleHighlights =
                                    stringResource(tutorialR.string.tutorial_p1_title_highlights).split(
                                        ',',
                                    ),
                            ) {
                                TutorialGraphicImage(
                                    modifier = Modifier.align(Alignment.Center),
                                    resId = R.drawable.graphic_tutorial_chat,
                                )
                            }
                        }

                        2 -> {
                            TutorialPage(
                                description = stringResource(tutorialR.string.tutorial_p2_desc),
                                title = stringResource(tutorialR.string.tutorial_p2_title),
                                titleHighlights =
                                    stringResource(tutorialR.string.tutorial_p2_title_highlights).split(
                                        ',',
                                    ),
                            ) {
                                TutorialGraphicImage(
                                    modifier = Modifier.align(Alignment.Center),
                                    resId = R.drawable.graphic_tutorial_timecapsule,
                                )
                            }
                        }

                        3 -> {
                            TutorialPage(
                                description = stringResource(tutorialR.string.tutorial_p3_desc),
                                title = stringResource(tutorialR.string.tutorial_p3_title),
                                titleHighlights =
                                    stringResource(tutorialR.string.tutorial_p3_title_highlights).split(
                                        ',',
                                    ),
                            ) {
                                TutorialGraphicImage(
                                    modifier =
                                        Modifier
                                            .align(Alignment.BottomCenter)
                                            .offset(y = 60.dp),
                                    resId = R.drawable.graphic_tutorial_report,
                                )

                                CtaButton(
                                    modifier =
                                        Modifier
                                            .align(Alignment.BottomCenter)
                                            .fillMaxWidth()
                                            .padding(horizontal = 15.dp),
                                    labelString = stringResource(tutorialR.string.tutorial_btn_start),
                                    onClick = navToLogin,
                                    isDefaultWidth = false,
                                )
                            }
                        }
                    }
                },
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
    title: String,
    modifier: Modifier = Modifier,
    titleHighlights: List<String> = emptyList(),
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier =
            modifier
                .fillMaxSize(),
    ) {
        content()

        Column(
            modifier =
                Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 15.dp)
                    .padding(top = 123.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.height(37.dp),
                style = MooiTheme.typography.body2,
                color = MooiTheme.colorScheme.gray500,
                text = description,
            )
            Text(
                modifier = Modifier.height(121.dp),
                textAlign = TextAlign.Center,
                style = MooiTheme.typography.head1,
                color = Color.White,
                text =
                    buildHighlightAnnotatedString(
                        fullString = title,
                        highlightWords = titleHighlights,
                        highlightStyle = SpanStyle(color = MooiTheme.colorScheme.primaryBlue500),
                    ),
            )
        }
    }
}

@Composable
private fun BoxScope.TutorialGraphicImage(
    @DrawableRes resId: Int,
    modifier: Modifier = Modifier,
) {
    Image(
        modifier =
            modifier
                .fillMaxSize(),
        painter = painterResource(resId),
        contentScale = ContentScale.Crop,
        contentDescription = null,
    )
}

@PreviewScreenSizes
@Composable
private fun TutorialPreview() {
    MooiTheme {
        TutorialScreen()
    }
}
