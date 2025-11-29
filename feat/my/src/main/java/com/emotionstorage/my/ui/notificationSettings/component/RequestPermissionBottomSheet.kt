package com.emotionstorage.my.ui.notificationSettings.component

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.ui.component.bottomSheet.BottomSheet
import com.emotionstorage.ui.theme.MooiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestPermissionBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    val context = LocalContext.current

    BottomSheet(
        modifier = modifier,
        sheetState = sheetState,
        shouldDismissOnBackPress = false,
        onDismissRequest = { /* do-nothing */ },
        subTitle = "앗, 알림이 꺼져 있어요!",
        title = "설정에서 알림을 켜주시면\n감정 기록 시간과 리포트를\n제때 전해드릴게요.\uD83C\uDF19",
        confirmLabel = "설정으로 이동하기",
        onConfirm = {
            // open system notification settings
            context.startActivity(
                Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                },
            )
            onDismiss()
        },
        forbidDismiss = true,
        hideDragHandle = true,
        sheetGesturesEnabled = false,
        contentPadding = PaddingValues(top = 23.dp, bottom = 41.dp, start = 15.dp, end = 15.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun RequestPermissionBottomSheetPreview() {
    MooiTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MooiTheme.colorScheme.background),
        )
        RequestPermissionBottomSheet(
            onDismiss = {},
            // open sheet state for preview
            sheetState =
                rememberStandardBottomSheetState(
                    initialValue = SheetValue.Expanded,
                ),
        )
    }
}
