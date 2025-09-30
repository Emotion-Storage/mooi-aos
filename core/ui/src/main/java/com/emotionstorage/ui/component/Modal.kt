package com.emotionstorage.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.emotionstorage.ui.theme.MooiTheme
import com.orhanobut.logger.Logger

@Composable
fun Modal(
    title: String,
    confirmLabel: String,
    onDismissRequest: () -> Unit,
    topDescription: String? = null,
    bottomDescription: String? = null,
    dismissLabel: String? = null,
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(MooiTheme.colorScheme.background)
                    .padding(top = 22.dp, bottom = 28.dp, start = 30.dp, end = 30.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // title & descriptions
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (!topDescription.isNullOrEmpty()) {
                    Text(
                        text = topDescription,
                        style = MooiTheme.typography.body2,
                        color = MooiTheme.colorScheme.gray500,
                        textAlign = TextAlign.Center,
                    )
                }
                Text(
                    text = title,
                    style =
                        MooiTheme.typography.head2.copy(
                            lineHeight = 30.sp,
                        ),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
                if (!bottomDescription.isNullOrEmpty()) {
                    Text(
                        text = bottomDescription,
                        style = MooiTheme.typography.body5,
                        color = MooiTheme.colorScheme.gray500,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            // buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ModalButton(
                    label = confirmLabel,
                    onClick = {
                        onConfirm()
                        onDismissRequest()
                    },
                )
                if (dismissLabel != null) {
                    ModalButton(
                        label = dismissLabel,
                        onClick = {
                            onDismiss()
                            onDismissRequest()
                        },
                        type = CtaButtonType.TONAL,
                    )
                }
            }
        }
    }
}

@Composable
private fun ModalButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: CtaButtonType = CtaButtonType.FILLED,
) {
    CtaButton(
        modifier = modifier.height(50.dp),
        label = label,
        onClick = onClick,
        type = type,
        radius = 10,
        textStyle = MooiTheme.typography.body2,
    )
}

@Preview(showBackground = true)
@Composable
private fun ModalPreview() {
    // background ui
    Box(modifier = Modifier.fillMaxSize())

    Modal(
        topDescription = "지금 돌아가면 회원가입을\n다시 시작해야 해요.",
        title = "그래도 메인 화면으로\n돌아갈까요?",
        confirmLabel = "회원가입을 계속 할게요.",
        dismissLabel = "메인 화면으로 나갈래요.",
        onDismissRequest = { },
        onConfirm = { },
        onDismiss = { },
    )
}
