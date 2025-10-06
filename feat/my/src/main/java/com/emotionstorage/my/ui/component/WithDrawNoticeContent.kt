package com.emotionstorage.my.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.component.CtaButtonType
import com.emotionstorage.ui.component.Modal
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun WithDrawNoticeContent(
    showSuccessDialog: Boolean,
    showFinalConfirmDialog: Boolean,
    onWithDrawButtonClick: () -> Unit,
    onBackClick: () -> Unit,
    onKeepClick: () -> Unit,
    onWithDrawClick: () -> Unit,
    onDismiss: () -> Unit,
    onFinalConfirmClick: () -> Unit,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                showBackButton = true,
                title = "회원탈퇴",
                handleBackPress = true,
                onBackClick = onBackClick,
            )
        },
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .matchParentSize()
                    .padding(innerPadding)
                    .background(MooiTheme.colorScheme.background),
            ) {

                WithDrawTitle()

                Spacer(modifier = Modifier.size(28.dp))

                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = "탈퇴 시 안내사항",
                    style = MooiTheme.typography.body7,
                    color = MooiTheme.colorScheme.gray400
                )

                Spacer(modifier = Modifier.size(12.dp))

                WithdrawBullets()

                if (showSuccessDialog) {
                    Modal(
                        title = "기록을 잠시 멈추고 싶다면,\n알림을 끄거나\n앱을 쉬어가보는 건 어떨까요?",
                        confirmLabel = "서비스를 탈퇴할래요.",
                        dismissLabel = "알림을 끄고 쉬어갈래요.",
                        onDismissRequest = onDismiss,
                        onConfirm = onWithDrawClick,
                        onDismiss = onKeepClick,
                        topDescription = null,
                    )
                }

                if (showFinalConfirmDialog) {
                    Modal(
                        title = "회원 탈퇴가\n완료되었습니다.",
                        confirmLabel = "확인",
                        onConfirm = onFinalConfirmClick,
                        onDismiss = {},
                        onDismissRequest = { },
                        topDescription = null,
                    )
                }
            }

            CtaButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .clickable { onWithDrawButtonClick() }
                    .padding(bottom = 28.dp),
                type = CtaButtonType.OUTLINED,
                labelString = "MOOI 서비스 탈퇴하기"
            )
        }
    }
}

@Composable
private fun WithDrawTitle() {
    val base = MooiTheme.typography.head1.copy(
        lineHeight = 36.sp,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )

    val title = buildAnnotatedString {
        append("정말 떠나시겠어요? 🥲\n")
        append("이 곳엔 ")

        withStyle(SpanStyle(color = MooiTheme.colorScheme.primary)) {
            append("당신의 감정들")
        }

        append("이\n차곡차곡 쌓여있어요.")
    }

    Text(
        text = title,
        style = base,
        color = Color.White,
        modifier = Modifier.padding(start = 16.dp, top = 22.dp, end = 16.dp)
    )
}

@Composable
private fun BulletItem(
    text: String,
    bulletSize: Dp = 4.dp,
    gap: Dp = 10.dp
) {
    val leadingWidth = bulletSize + gap
    Row(Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .width(leadingWidth)
                .padding(top = 6.dp)
        ) {
            Canvas(Modifier.size(bulletSize)) { drawCircle(color = Color(0xFFDADADA)) }
        }

        Text(
            text = text,
            style = MooiTheme.typography.body8.copy(
                lineHeight = 22.sp,
                platformStyle = PlatformTextStyle(includeFontPadding = false)
            ),
            color = MooiTheme.colorScheme.gray300,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}

@Composable
private fun WithdrawBullets() {
    val items = listOf(
        "탈퇴 즉시 모든 감정 타임캡슐 및 활동 기록이\n삭제돼요.",
        "탈퇴 후에는 복구가 불가능하며, 다시 가입해도\n이전 데이터는 돌아오지 않아요.",
        "단, 시스템 안정성을 위해 최소한의 정보는\n6개월간 안전하게 보관돼요."
    )
    Column(
        Modifier.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) { items.forEach { BulletItem(it) } }
}


@Preview
@Composable
private fun WithDrawTitlePreview() {
    MooiTheme {
        WithDrawTitle()
    }
}

@Preview
@Composable
private fun WithDrawNoticeContentPreview() {
    MooiTheme {
        WithDrawNoticeContent(
            showSuccessDialog = false,
            showFinalConfirmDialog = false,
            onWithDrawButtonClick = {},
            onBackClick = {},
            onKeepClick = {},
            onWithDrawClick = {},
            onDismiss = {},
            onFinalConfirmClick = {},
        )
    }
}
