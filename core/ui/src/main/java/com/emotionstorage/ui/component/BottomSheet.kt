package com.emotionstorage.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.theme.MooiTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    title: String? = null,
    subTitle: String? = null,
    confirmLabel: String? = null,
    onConfirm: (() -> Unit)? = null,
    dismissLabel: String? = null,
    onDismiss: (() -> Unit)? = null,
    hideDragHandle: Boolean = false,
    content: @Composable (ColumnScope.() -> Unit)? = null,
) {
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        dragHandle =
            if (hideDragHandle) {
                null
            } else {
                {
                    BottomSheetDefaults.DragHandle(
                        width = 35.dp,
                        height = 3.dp,
                        color = MooiTheme.colorScheme.gray500,
                        shape = RoundedCornerShape(100),
                    )
                }
            },
        // disable gesture if drag handle is hidden
        sheetGesturesEnabled = !hideDragHandle,
        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        containerColor = MooiTheme.colorScheme.blueGrayBackground,
        contentColor = Color.White,
    ) {
        Column(
            modifier =
                Modifier
                    .padding(top = 23.dp, bottom = 59.dp, start = 15.dp, end = 15.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // title
            if (!subTitle.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.padding(bottom = 5.dp),
                    text = subTitle,
                    style = MooiTheme.typography.body2,
                    color = MooiTheme.colorScheme.gray500,
                    textAlign = TextAlign.Center,
                )
            }
            if (!title.isNullOrEmpty()) {
                Text(
                    modifier = Modifier.padding(bottom = 19.dp),
                    text = title,
                    style =
                        MooiTheme.typography.head2.copy(
                            fontSize = 22.sp,
                            lineHeight = 30.sp,
                        ),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                )
            }

            // content
            content?.invoke(this)

            // buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (confirmLabel != null) {
                    CtaButton(
                        modifier = Modifier.fillMaxWidth(),
                        label = confirmLabel,
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onConfirm?.invoke()
                                }
                            }
                        },
                    )
                }
                if (dismissLabel != null) {
                    CtaButton(
                        modifier = Modifier.fillMaxWidth(),
                        label = dismissLabel,
                        onClick = {
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDismiss?.invoke()
                                }
                            }
                        },
                        type = CtaButtonType.TONAL,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun BottomSheetPreview() {
    val (showBottomSheet, setShowBottomSheet) = remember { mutableStateOf(false) }

    MooiTheme {
        Scaffold(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background),
        ) { innerPadding ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(MooiTheme.colorScheme.background)
                        .padding(innerPadding),
            ) {
                Button(
                    modifier = Modifier.align(Alignment.TopCenter),
                    onClick = {
                        setShowBottomSheet(true)
                    },
                ) {
                    Text(text = "show bottom sheet")
                }
            }
            if (showBottomSheet) {
                BottomSheet(
                    onDismissRequest = {
                        setShowBottomSheet(false)
                    },
                    hideDragHandle = true,
                    title = "대화를 종료하고,\n지금까지의 감정을 정리해볼까요?",
                    subTitle = "감정을 충분히 이야기했어요.",
                    confirmLabel = "네, 종료할래요.",
                    onConfirm = {
                        setShowBottomSheet(false)
                    },
                    dismissLabel = "아니요, 더 이야기할래요.",
                    onDismiss = {
                        setShowBottomSheet(false)
                    },
                )
            }
        }
    }
}
