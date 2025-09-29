package com.emotionstorage.time_capsule.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.common.toKorDate
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.domain.model.TimeCapsule.STATUS
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.component.BottomSheet
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.theme.pretendard
import com.emotionstorage.ui.util.mainBackground
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.String

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeCapsuleCalendarBottomSheet(
    date: LocalDate,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    timeCapsules: List<TimeCapsuleItemState> = emptyList(),
    navToTimeCapsuleDetail: (id: String) -> Unit = {},
    navToDailyReport: (() -> Unit)? = null,
    isNewDailyReport: Boolean = false,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val scrollState = rememberScrollState()

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
                    .padding(bottom = 17.dp),
            text = date.toKorDate(),
            textAlign = TextAlign.Start,
        )
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .heightIn(max = (screenHeight / 2).dp)
                    .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(32.dp),
        ) {
            timeCapsules.forEach {
                TimeCapsuleItem(
                    modifier = Modifier.fillMaxWidth(),
                    timeCapsule = it,
                    onClick = { navToTimeCapsuleDetail(it.id) },
                )
            }
            Spacer(modifier = Modifier.height(21.dp))
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
    Box(
        modifier =
            modifier
                .height((66.33).dp)
                .mainBackground(enabled, RoundedCornerShape(15.dp), MooiTheme.colorScheme.gray700)
                .clickable(enabled = enabled, onClick = { onClick?.invoke() }),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "일일 리포트 확인하기",
            style = MooiTheme.typography.button,
            color = if (enabled) Color.White else MooiTheme.colorScheme.gray500,
        )
        if (isNewDailyReport && enabled) {
            Box(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .offset(x = 78.dp, y = -13.dp)
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
    val timeCapsules =
        (0..3).map { i ->
            TimeCapsuleItemState(
                id = i.toString(),
                status = STATUS.entries.get(i),
                title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                emotions =
                    listOf(
                        Emotion(
                            label = "서운함",
                            icon = 0,
                        ),
                        Emotion(
                            label = "화남",
                            icon = 1,
                        ),
                        Emotion(
                            label = "피곤함",
                            icon = 2,
                        ),
                    ),
                isFavorite = false,
                isFavoriteAt = null,
                createdAt = LocalDateTime.now(),
                openDDay = 10,
            )
        }

    MooiTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background),
        ) {
            TimeCapsuleCalendarBottomSheet(
                // open sheet state for preview
                sheetState =
                    rememberStandardBottomSheetState(
                        initialValue = SheetValue.Expanded,
                    ),
                date = LocalDate.now(),
                onDismissRequest = {},
                timeCapsules = timeCapsules,
                navToTimeCapsuleDetail = {},
                navToDailyReport = {},
                isNewDailyReport = true,
            )
        }
    }
}
