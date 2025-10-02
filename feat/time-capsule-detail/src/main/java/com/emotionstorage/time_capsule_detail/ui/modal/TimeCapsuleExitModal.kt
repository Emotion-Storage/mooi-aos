package com.emotionstorage.time_capsule_detail.ui.modal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.emotionstorage.ui.component.CtaButton
import com.emotionstorage.ui.component.CtaButtonType
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun TimeCapsuleExitModal(
    isModalOpen: Boolean = false,
    onDismissRequest: () -> Unit = {},
    onContinue: () -> Unit = {},
    onExit: () -> Unit = {},
) {
    if (isModalOpen) {
        // custom modal
        Dialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(MooiTheme.colorScheme.background)
                        .padding(top = 23.dp, bottom = 28.dp)
                        .padding(horizontal = 25.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // title & descriptions
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text =
                            buildAnnotatedString {
                                append("타임캡슐을 보관하지 않으면,\n")
                                append("감정은 임시 저장되며\n")
                                withStyle(
                                    SpanStyle(
                                        color = MooiTheme.colorScheme.primary,
                                    ),
                                ) {
                                    append("24시간 후 자동 삭제")
                                }
                                append("돼요.")
                            },
                        style = MooiTheme.typography.body1.copy(lineHeight = 22.sp),
                        color = MooiTheme.colorScheme.gray300,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        modifier = Modifier.padding(top = 9.dp, bottom = 15.dp),
                        text = "* 그 전까지는 언제든 다시 보관할 수 있어요.",
                        style = MooiTheme.typography.body8.copy(lineHeight = 24.sp),
                        color = MooiTheme.colorScheme.gray500,
                    )
                    Text(
                        modifier = Modifier.height(30.dp),
                        text = "지금 페이지를 나가시겠어요?",
                        style = MooiTheme.typography.head2,
                        color = Color.White,
                    )
                }
                // buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    CtaButton(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                        labelString = "아니요, 계속할래요.",
                        onClick = onContinue,
                        radius = 10,
                        isDefaultWidth = false,
                        isDefaultHeight = false,
                    )
                    CtaButton(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                        labelString = "네, 그냥 나갈래요.",
                        onClick = onExit,
                        radius = 10,
                        isDefaultWidth = false,
                        isDefaultHeight = false,
                        type = CtaButtonType.TONAL,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TimeCapsuleExitModalPreview() {
    // background ui
    Box(modifier = Modifier.fillMaxSize())

    TimeCapsuleExitModal(
        isModalOpen = true,
    )
}
