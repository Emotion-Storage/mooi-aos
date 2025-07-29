package com.emtionstorage.tutorial.ui.component

import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.theme.EmotionStorageTheme
import com.emotionstorage.ui.theme.MooiTheme
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
    pages: List<@Composable (ColumnScope.() -> Unit)> = emptyList(),
) {
    val pagerState = rememberPagerState(pageCount = {
        pageCount
    })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalPager(state = pagerState, modifier=Modifier.weight(1f)) { page ->
            pages[page]()
        }

        Box(modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .height(48.dp)
            .padding(top=30.dp)) {
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
                    text = "건너뛰기"
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
    // todo: animate color change
    // todo: 너비 애니메이션 실행될 때, 테두리 radius 설정 해제되는 문제 해결
    Box(
        modifier = modifier
            .animateContentSize()
            .height(10.dp)
            .width(if (isSelected) 31.dp else 10.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(if (isSelected) MooiTheme.colorScheme.primary else MooiTheme.colorScheme.gray50)
            .clickable(onClick = onClick),
    )
}

//@Preview
//@Composable
//private fun PagerIndicatorDotPreview() {
//    val isDotSelected = remember { mutableStateOf(false) }
//
//    EmotionStorageTheme {
//        PagerIndicatorDot(
//            isSelected = isDotSelected.value,
//            onClick = {
//                isDotSelected.value = !isDotSelected.value
//            }
//        )
//    }
//}

@Preview(showBackground = true)
@Composable
private fun PagerWithIndicatorPreview() {
    EmotionStorageTheme {
        PagerWithIndicator(
            modifier = Modifier.fillMaxSize(),
            pageCount = 3,
            pages = listOf(
                {
                    Text(
                        modifier = Modifier
                            .background(MooiTheme.colorScheme.background)
                            .fillMaxSize(),
                        color = Color.White,
                        text = "Page 0"
                    )
                },
                {
                    Text(
                        modifier = Modifier
                            .background(MooiTheme.colorScheme.background)
                            .fillMaxSize(),
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        text = "Page 1"
                    )
                },
                {
                    Text(
                        modifier = Modifier
                            .background(MooiTheme.colorScheme.background)
                            .fillMaxSize(),
                        color = Color.White,
                        text = "Page 2"
                    )
                }
            )
        )
    }
}