package com.emotionstorage.time_capsule.ui.component

import androidx.compose.foundation.background
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
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.model.TimeCapsule.Status
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.component.BottomSheet
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.ui.theme.pretendard
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
    onToggleFavorite: (id: String) -> Unit = {},
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
                    .fillMaxWidth(),
            text = date.toKorDate(),
            style = MooiTheme.typography.body4,
            textAlign = TextAlign.Start,
        )

        Spacer(modifier = Modifier.size(18.dp))

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
                    onFavoriteClick = { onToggleFavorite(it.id) },
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
    val timeCapsules =
        (0..3).map { i ->
            TimeCapsuleItemState(
                id = i.toString(),
                status = Status.entries.get(i),
                title = "오늘 아침에 친구를 만났는데, 친구가 늦었어..",
                emotions =
                    listOf(
                        TimeCapsule.Emotion(
                            emoji = "\uD83D\uDE14",
                            label = "서운함",
                            percentage = 30.0f,
                        ),
                        TimeCapsule.Emotion(
                            emoji = "\uD83D\uDE0A",
                            label = "고마움",
                            percentage = 30.0f,
                        ),
                        TimeCapsule.Emotion(
                            emoji = "\uD83E\uDD70",
                            label = "안정감",
                            percentage = 80.0f,
                        ),
                    ),
                isFavorite = false,
                isFavoriteAt = null,
                createdAt = LocalDateTime.now(),
                expireAt = LocalDateTime.now().plusHours(5),
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
