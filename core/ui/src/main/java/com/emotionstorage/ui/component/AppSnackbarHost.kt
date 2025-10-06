package com.emotionstorage.ui.component

import android.view.Gravity
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme

private const val SNACKBAR_VERTICAL_PADDING = 30

@Composable
fun AppSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    gravity: Int = Gravity.BOTTOM,

    paddingValues: PaddingValues = PaddingValues(vertical = SNACKBAR_VERTICAL_PADDING.dp),
    snackbarIconId: Int? = null,
    hostContent: @Composable (snackbarData: SnackbarData) -> Unit = {
        Toast(
            message = it.visuals.message,
            iconId = snackbarIconId,
        )
    },
) {
    val dismissState =
        rememberSwipeToDismissBoxState(
            confirmValueChange = { value ->
                if (value != SwipeToDismissBoxValue.Settled) {
                    hostState.currentSnackbarData?.dismiss()
                    true
                } else {
                    false
                }
            },
        )

    LaunchedEffect(hostState.currentSnackbarData) {
        hostState.currentSnackbarData?.let {
            dismissState.reset()
        }
    }

    hostState
        .currentSnackbarData
        ?.takeIf { it.visuals.message.isNotEmpty() }
        ?.let { data ->
            Dialog(
                onDismissRequest = {
                    data.dismiss()
                },
                properties =
                    DialogProperties(
                        dismissOnClickOutside = true,
                        dismissOnBackPress = true,
                        usePlatformDefaultWidth = false,
                    ),
            ) {
                // Position the dialog at the bottom of the screen
                (LocalView.current.parent as DialogWindowProvider).window.apply {
                    setGravity(gravity)
                    clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND) // Remove the dim background
                    // Don't add FLAG_NOT_TOUCHABLE so swipe gestures work
                    // Don't add FLAG_NOT_FOCUSABLE so clicks outside can dismiss

                    // Set layout parameters to position at bottom with proper width
                    attributes =
                        attributes.apply {
                            width = WindowManager.LayoutParams.MATCH_PARENT
                            // No need to set height as it will wrap content
                        }
                }

                val density = LocalDensity.current
                val navigationBarPadding =
                    with(density) {
                        WindowInsets.navigationBars.getBottom(this).toDp()
                    }

                SwipeToDismissBox(
                    modifier =
                        modifier
                            .padding(bottom = navigationBarPadding)
                            .padding(paddingValues),
                    state = dismissState,
                    backgroundContent = {},
                ) {
                    SnackbarHost(hostState = hostState) {
                        hostContent(it)
                    }
                }
            }
        }
}

@Composable
fun Toast(
    message: String,
    modifier: Modifier = Modifier,
    iconId: Int? = null,
    paddingValues: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 13.dp),
) {
    Row(
        modifier =
            modifier
                .background(
                    Color(0xFF0E0C12).copy(alpha = 0.8f),
                    RoundedCornerShape(100),
                )
                .padding(paddingValues),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (iconId != null) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(iconId),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(9.dp))
        }

        Text(
            text = message,
            style = MooiTheme.typography.body7.copy(lineHeight = 24.sp),
            color = Color.White,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun ToastPreview() {
    MooiTheme {
        Column(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.background)
                    .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Toast("즐겨찾기가 설정되었습니다.", iconId = R.drawable.success_filled)
            Toast("내 마음 서랍이 꽉 찼어요. 😢\n즐겨찾기 중 일부를 해제해주세요.")
            Toast(
                "아직 보관을 확정하지 않은 감정이에요.\n오늘을 기준으로 타임캡슐\n회고 날짜를 지정해주세요.",
                paddingValues = PaddingValues(horizontal = 25.dp, vertical = 13.dp),
            )
        }
    }
}
