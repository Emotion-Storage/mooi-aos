package com.emotionstorage.home.ui.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.ui.component.modal.Modal
import com.emotionstorage.ui.theme.MooiTheme

/**
 * attendance_02
 * - Case : 출석 보상
 *      - attendance_01 팝업 표시 상태에서 날짜가 변경 되었을 때
 *      - 미묘한 타이밍으로 [보상 받기]를 눌렀을 경우 표출
 *      => (11시 59분 접속 후 popup 누르지 않은 경우에 Alert 하기 위한 용도)
 *
 * - 표시 화면
 *      - 3.1 메인(홈) 화면
 *
 * - CTA 및 이동
 *      - [새로운 보상 확인하기] -> 팝업 닫히며 새로운 날짜의 출석보상 팝업(attendance_01) 노출
 *
 * - 차단
 *      - 뒤로가기 x
 *      - 배경 터치 X
 */

@Composable
fun AttendanceRefreshModal(onConfirm: () -> Unit) {
    Modal(
        onDismissRequest = {},
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        title = "날짜가 바뀌었어요!",
        bottomDescription = "오늘의 출석 보상은\n새로고침 후 다시 확인해주세요.",
        confirmLabel = "새로운 보상 확인하기",
        onConfirm = onConfirm,
    )
}

@Preview
@Composable
private fun AttendanceRefreshModalPreview() {
    MooiTheme {
        AttendanceRefreshModal(
            onConfirm = {},
        )
    }
}
