package com.emotionstorage.time_capsule.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.emotionstorage.common.toKorDateWithWeekDay
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.component.bottomSheet.BottomSheet
import com.emotionstorage.ui.component.button.CtaButton
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.theme.pretendard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeCapsuleBottomSheet(
    date: LocalDate,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    timeCapsulesFlow: Flow<PagingData<TimeCapsuleItemState>>? = null,
    onToggleFavorite: (id: Long, prevFavorite: Boolean) -> Unit = { _, _ -> },
    navToTimeCapsuleDetail: (id: Long) -> Unit = {},
    navToDailyReport: (() -> Unit)? = null,
    isNewDailyReport: Boolean = false,
) {
    val timeCapsules = timeCapsulesFlow?.collectAsLazyPagingItems()

    BottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        contentPadding = PaddingValues(top = 7.dp, start = 15.dp, end = 15.dp, bottom = (39.7).dp),
    ) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 1.dp),
            text = date.toKorDateWithWeekDay(),
            style = MooiTheme.typography.body4,
            textAlign = TextAlign.Start,
        )

        Spacer(modifier = Modifier.size(18.dp))

        LazyColumn(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .heightIn(max = 500.dp)
                    .padding(horizontal = 1.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp),
            contentPadding = PaddingValues(bottom = 51.dp),
        ) {
            when (timeCapsules?.loadState?.refresh) {
                is LoadState.NotLoading -> {
                    if (timeCapsules.itemCount > 0) {
                        items(count = timeCapsules.itemCount, key = { timeCapsules[it]?.id ?: it }) {
                            timeCapsules[it]?.run {
                                TimeCapsuleItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    timeCapsule = this,
                                    onClick = { navToTimeCapsuleDetail(this.id) },
                                    onFavoriteClick = { onToggleFavorite(this.id, this.isFavorite) },
                                )
                            }
                        }
                    }
                }

                else -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(vertical = 20.dp),
                            color = MooiTheme.colorScheme.primary,
                        )
                    }
                    // todo: add error ui
                }
            }
        }

        DailyReportButton(
            modifier = Modifier.fillMaxWidth(),
            enabled = navToDailyReport != null,
            isNewDailyReport = isNewDailyReport,
            onClick = navToDailyReport,
        )
    }
}

@Composable
private fun DailyReportButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isNewDailyReport: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    CtaButton(
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        onClick = { onClick?.invoke() },
        isDefaultWidth = false,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "일일 리포트 확인하기",
                style = MooiTheme.typography.mainButton,
                color = if (enabled) Color.White else MooiTheme.colorScheme.gray500,
            )
            if (isNewDailyReport && enabled) {
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.Center)
                            .offset(x = 78.dp, y = -12.dp)
                            .size(20.dp)
                            .background(Color(0xFF1C1A22).copy(alpha = 0.5f), CircleShape),
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "N",
                        style =
                            TextStyle(
                                fontFamily = pretendard,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp,
                            ),
                        color = Color.White,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DailyReportButtonPreview() {
    MooiTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MooiTheme.colorScheme.background)
                    .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            DailyReportButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
                isNewDailyReport = true,
                onClick = {},
            )
            DailyReportButton(
                modifier = Modifier.fillMaxWidth(),
                enabled = false,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun TimeCapsuleCalendarBottomSheetPreview() {
    MooiTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background),
        ) {
            TimeCapsuleBottomSheet(
                // open sheet state for preview
                sheetState =
                    rememberStandardBottomSheetState(
                        initialValue = SheetValue.Expanded,
                    ),
                date = LocalDate.now(),
                onDismissRequest = {},
                timeCapsulesFlow = emptyFlow(),
                navToTimeCapsuleDetail = {},
                navToDailyReport = {},
                isNewDailyReport = true,
            )
        }
    }
}
