package com.emotionstorage.tutorial.ui.modal

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.emotionstorage.tutorial.R
import com.emotionstorage.ui.component.modal.Modal

/**
 * onboarding_01
 * - Case : 회원가입/온보딩 중단
 *     - 온보딩에서 뒤로가기를 계속 시도하여 최종 나가기를 시도할 경우 표출
 *     - (최종 나가기 : 첫 페이지인 온보딩 1단계)
 * - 표시 화면
 *     - 2.2 온보딩(1) - 닉네임 설정
 * - CTA 및 이동
 *     - [회원가입을 계속 할게요.] -> 팝업 닫히며 이동 X(온보딩 화면 그대로)
 *     - [로그인 화면으로 나갈래요.] -> 팝업 닫히며 로그인 화면으로 이동
 * - 차단
 *     - 뒤로가기(Android) O (팝업 닫힘, 화면 제자리)
 *     - 배경 터치 O (팝업 닫힘, 화면 제자리)
 */
@Composable
fun OnBoardingExitModel(
    onDismissRequest: () -> Unit = {},
    onExit: () -> Unit = {},
) {
    Modal(
        topDescription = stringResource(R.string.on_boarding_exit_modal_desc),
        title = stringResource(R.string.on_boarding_exit_modal_title),
        confirmLabel = stringResource(R.string.on_boarding_exit_modal_confirm),
        dismissLabel = stringResource(R.string.on_boarding_exit_modal_dismiss),
        onDismissRequest = onDismissRequest,
        onDismiss = onExit,
    )
}
