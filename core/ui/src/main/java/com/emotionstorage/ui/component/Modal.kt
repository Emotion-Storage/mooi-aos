package com.emotionstorage.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun Modal(
    onDismissRequest: () -> Unit,
    topDescription: String? = null,
    title: String? = null,
    bottomDescription: String? = null,
    confirmLabel: String? = null,
    onConfirm: () -> Unit = {},
    dismissLabel: String? = null,
    onDismiss: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(top = 22.dp, bottom = 28.dp, start = 30.dp, end = 30.dp),
    content: @Composable (() -> Unit)? = null,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        // set dim amount to 0.8f
        (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0.8f)

        Column(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(MooiTheme.colorScheme.background)
                    .padding(contentPadding)
                    .widthIn(max = 293.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // title & descriptions
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (!topDescription.isNullOrEmpty()) {
                    Text(
                        text = topDescription,
                        style = MooiTheme.typography.body5,
                        color = MooiTheme.colorScheme.gray500,
                        textAlign = TextAlign.Center,
                    )
                }
                if (!title.isNullOrEmpty()) {
                    Text(
                        text = title,
                        style =
                            MooiTheme.typography.head2.copy(
                                lineHeight = 30.sp,
                            ),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                    )
                }
                if (!bottomDescription.isNullOrEmpty()) {
                    Text(
                        text = bottomDescription,
                        style = MooiTheme.typography.body5,
                        color = MooiTheme.colorScheme.gray500,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            content?.invoke()

            // buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (confirmLabel != null) {
                    CtaButton(
                        modifier = Modifier.height(50.dp),
                        labelString = confirmLabel,
                        onClick = {
                            onConfirm()
                            onDismissRequest()
                        },
                        radius = 10,
                        textStyle = MooiTheme.typography.mainButton,
                    )
                }
                if (dismissLabel != null) {
                    CtaButton(
                        modifier = Modifier.height(50.dp),
                        labelString = dismissLabel,
                        onClick = {
                            onDismiss()
                            onDismissRequest()
                        },
                        type = CtaButtonType.TONAL,
                        radius = 10,
                        textStyle = MooiTheme.typography.mainButton,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ModalPreview() {
    // background ui
    Box(modifier = Modifier.fillMaxSize())

    Modal(
        topDescription = "지금 돌아가면 회원가입을\n다시 시작해야 해요.",
        title = "그래도 로그인 화면으로\n돌아갈까요?",
        confirmLabel = "회원가입을 계속 할게요.",
        dismissLabel = "로그인 화면으로 나갈래요.",
        onDismissRequest = { },
        onConfirm = { },
        onDismiss = { },
    )
}
