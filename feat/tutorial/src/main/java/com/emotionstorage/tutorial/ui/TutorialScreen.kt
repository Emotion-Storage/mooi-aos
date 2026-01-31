package com.emotionstorage.tutorial.ui

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.emotionstorage.tutorial.ui.component.PagerWithIndicator
import com.emotionstorage.tutorial.ui.util.TutorialResponsiveTokens
import com.emotionstorage.tutorial.ui.util.clamp
import com.emotionstorage.tutorial.ui.util.scaledBy
import com.emotionstorage.ui.R
import com.emotionstorage.ui.annotation.PreviewScreenRatios
import com.emotionstorage.ui.component.button.CtaButton
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.GUIDE_HEIGHT
import com.emotionstorage.ui.util.buildHighlightAnnotatedString
import com.emotionstorage.tutorial.R as tutorialR

private const val TUTORIAL_PAGE_COUNT = 4

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun TutorialScreen(
    modifier: Modifier = Modifier,
    navToLogin: () -> Unit = {},
) {
//    NotificationPermissionAutoRequest()

    BoxWithConstraints(
        modifier =
            modifier
                .background(MooiTheme.colorScheme.backgroundDefault)
                .fillMaxSize(),
    ) {
        val safePadding = WindowInsets.safeDrawing.asPaddingValues()
        val topInset = safePadding.calculateTopPadding()
        val bottomInset = safePadding.calculateBottomPadding()

        val safeHeight = (maxHeight - topInset - bottomInset).coerceAtLeast(0.dp)
        val scaleY = (safeHeight / GUIDE_HEIGHT.dp).coerceAtLeast(0f)

        val topFromStatus = 78.dp.scaledBy(scaleY).clamp(56.dp, 120.dp)
        val bottomFromNav = 41.dp.scaledBy(scaleY).clamp(28.dp, 72.dp)

        val descriptionHeight = 37.dp.scaledBy(scaleY).clamp(28.dp, 52.dp)
        val titleHeight = 121.dp.scaledBy(scaleY).clamp(88.dp, 160.dp)

        val indicatorHeight = 48.dp
        val ctaBottomPadding = 30.dp

        val tokens =
            TutorialResponsiveTokens(
                insets = safePadding,
                topPadding = topInset + topFromStatus,
                bottomPadding = bottomInset + bottomFromNav,
                descriptionHeight = descriptionHeight,
                titleHeight = titleHeight,
            )

        PagerWithIndicator(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.backgroundDefault)
                    .fillMaxSize(),
            tokens = tokens,
            pageCount = TUTORIAL_PAGE_COUNT,
            pageContent =
                { page ->
                    when (page) {
                        0 -> {
                            TutorialPage(
                                tokens = tokens,
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
                                tokens = tokens,
                                description = stringResource(tutorialR.string.tutorial_p1_desc),
                                title = stringResource(tutorialR.string.tutorial_p1_title),
                                titleHighlights =
                                    stringResource(tutorialR.string.tutorial_p1_title_highlights).split(
                                        ',',
                                    ),
                            ) {
                                TutorialGraphicImage(
                                    modifier = Modifier,
                                    resId = R.drawable.graphic_tutorial_chat,
                                )
                            }
                        }

                        2 -> {
                            TutorialPage(
                                tokens = tokens,
                                description = stringResource(tutorialR.string.tutorial_p2_desc),
                                title = stringResource(tutorialR.string.tutorial_p2_title),
                                titleHighlights =
                                    stringResource(tutorialR.string.tutorial_p2_title_highlights).split(
                                        ',',
                                    ),
                            ) {
                                TutorialGraphicImage(
                                    modifier = Modifier,
                                    resId = R.drawable.graphic_tutorial_timecapsule,
                                )
                            }
                        }

                        3 -> {
                            TutorialPage(
                                tokens = tokens,
                                contentLift = ctaBottomPadding,
                                description = stringResource(tutorialR.string.tutorial_p3_desc),
                                title = stringResource(tutorialR.string.tutorial_p3_title),
                                titleHighlights =
                                    stringResource(tutorialR.string.tutorial_p3_title_highlights).split(
                                        ',',
                                    ),
                            ) {
                                TutorialGraphicImage(
                                    modifier = Modifier,
                                    resId = R.drawable.graphic_tutorial_report,
                                )

                                CtaButton(
                                    modifier =
                                        Modifier
                                            .align(Alignment.BottomCenter)
                                            .fillMaxWidth()
                                            .padding(horizontal = 15.dp)
                                            .padding(bottom = bottomFromNav + indicatorHeight + ctaBottomPadding),
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
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun ColumnScope.TutorialPage(
    tokens: TutorialResponsiveTokens,
    description: String,
    title: String,
    modifier: Modifier = Modifier,
    titleHighlights: List<String> = emptyList(),
    contentLift: Dp = 18.dp,
    content: @Composable BoxScope.() -> Unit = {},
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .offset(y = -contentLift),
            ) {
                content()
            }

            Column(
                modifier =
                    Modifier
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 24.dp)
                        .padding(top = tokens.topPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    modifier = Modifier.height(tokens.descriptionHeight),
                    style = MooiTheme.typography.body2,
                    color = MooiTheme.colorScheme.gray500,
                    text = description,
                )
                Text(
                    modifier = Modifier.height(tokens.titleHeight),
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
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun BoxScope.TutorialGraphicImage(
    modifier: Modifier = Modifier,
    @DrawableRes resId: Int,
) {
    Box(
        modifier
            .fillMaxSize()
            .clipToBounds(),
    ) {
        Image(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(360f / 752f, matchHeightConstraintsFirst = true),
            painter = painterResource(resId),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            alignment = Alignment.TopCenter,
        )
    }
}

@PreviewScreenRatios
@Composable
private fun TutorialPreview() {
    MooiTheme {
        TutorialScreen()
    }
}
