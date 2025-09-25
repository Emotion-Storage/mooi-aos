package com.emotionstorage.time_capsule.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.common.toKorDate
import com.emotionstorage.domain.model.TimeCapsule.Emotion
import com.emotionstorage.domain.model.TimeCapsule.STATUS
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.ui.component.BottomSheet
import com.emotionstorage.ui.theme.MooiTheme
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.String

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeCapsuleCalendarBottomSheet(
    date: LocalDate,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    timeCapsules: List<TimeCapsuleItemState> = emptyList(),
    navToTimeCapsuleDetail: (id: String) -> Unit = {},
    // 일일 리포트 생성된 경우, 일일 리포트 화면으로 이동
    navToDailyReport: (() -> Unit)? = null,
    // 일일 리포트 확인 여부, 미확인인 경우 n 뱃지 표시
    isNewDailyReport: Boolean = false,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val scrollState = rememberScrollState()

    BottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle = {
            Box(
                modifier =
                    Modifier
                        .size(35.dp, 3.dp)
                        .background(
                            MooiTheme.colorScheme.gray500,
                            RoundedCornerShape(100),
                        ),
            )
        },
        // todo: add new badge
        confirmLabel = if (navToDailyReport != null) "일일리포트 확인하기" else null,
        onConfirm = navToDailyReport,
        hideOnConfirm = false,
        dismissLabel = if (navToDailyReport == null) "일일리포트 확인하기" else null,
        hideOnDismiss = false,
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
                    .padding(bottom = 50.dp)
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
                openDday = -99,
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
