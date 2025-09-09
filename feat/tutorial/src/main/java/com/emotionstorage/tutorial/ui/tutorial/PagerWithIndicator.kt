package com.emotionstorage.tutorial.ui.tutorial

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.tutorial.R
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.util.mainBackground
import kotlinx.coroutines.launch

/**
 * PagerWithDotIndicator
 * @param pageCount 페이지 총 개수
 * @param modifier Modifier
 * @param pages 페이지 Composable 함수 목록
 */
@Composable
fun PagerWithIndicator(
    pageCount: Int,
    modifier: Modifier = Modifier,
    pageContent: @Composable (ColumnScope.(page: Int) -> Unit)
) {
    val pagerState = rememberPagerState(pageCount = {
        pageCount
    })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.weight(1f)) { page ->
            pageContent(page)
        }

        Box(
            modifier = Modifier
                .background(Color.Transparent)
                .fillMaxWidth()
                .height(48.dp)
                .padding(top = 30.dp)
        ) {
            PagerIndicator(
                modifier = Modifier
                    .align(Alignment.Center),
                pageCount = pageCount,
                currentPage = pagerState.currentPage,
                onPageSelected = {
                    coroutineScope.launch {
                        pagerState.scrollToPage(it)
                    }
                }
            )

            if (pagerState.currentPage != pageCount - 1) {
                Text(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp)
                        .clickable(
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.scrollToPage(pageCount - 1)
                                }
                            }),
                    color = MooiTheme.colorScheme.gray600,
                    text = stringResource(R.string.pager_btn_skip),
                    style = MooiTheme.typography.body3.copy(fontSize = 14.sp, lineHeight = 22.sp)
                )
            }
        }
    }
}

@Composable
private fun PagerIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int = 0,
    onPageSelected: (Int) -> Unit = {},
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        for (i in 0 until pageCount) {
            PagerIndicatorDot(
                isSelected = currentPage == i,
                onClick = { onPageSelected(i) }
            )
        }
    }
}

@Composable
private fun PagerIndicatorDot(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    val animatedWidth by animateDpAsState(
        targetValue = if (isSelected) 31.dp else 10.dp,
        animationSpec = tween(durationMillis = 500), label = "widthAnimation"
    )

    Box(
        modifier = modifier
            .height(10.dp)
            .width(animatedWidth)
            .mainBackground(isSelected, RoundedCornerShape(50.dp), MooiTheme.colorScheme.gray50)
            .clickable(onClick = onClick),
    )
}

@Preview
@Composable
private fun PagerIndicatorDotPreview() {
    val (selectedDot, setSelectedDot) = remember { mutableStateOf(0) }

    MooiTheme {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .background(MooiTheme.colorScheme.background),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PagerIndicatorDot(
                isSelected = selectedDot == 0,
                onClick = {
                    setSelectedDot(0)
                }
            )
            PagerIndicatorDot(
                isSelected = selectedDot == 1,
                onClick = {
                    setSelectedDot(1)
                }
            )
            PagerIndicatorDot(
                isSelected = selectedDot == 2,
                onClick = {
                    setSelectedDot(2)
                }
            )
            PagerIndicatorDot(
                isSelected = selectedDot == 3,
                onClick = {
                    setSelectedDot(3)
                }
            )
        }
    }
}