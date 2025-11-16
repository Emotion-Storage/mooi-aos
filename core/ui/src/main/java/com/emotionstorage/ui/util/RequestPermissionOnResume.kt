package com.emotionstorage.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.orhanobut.logger.Logger

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissionOnResume(
    permission: String,
    onPermissionGranted: () -> Unit = {},
    onPermissionDenied: (showRationale: Boolean) -> Unit = {}
) {
    val permissionState =
        rememberPermissionState(permission = permission)

    LifecycleResumeEffect("onResume") {
        permissionState.launchPermissionRequest()
        onPauseOrDispose {}
    }

    LaunchedEffect(permissionState.status) {
        when (permissionState.status) {
            is PermissionStatus.Granted -> {
                Logger.d("Permission granted")
                onPermissionGranted()
            }

            is PermissionStatus.Denied -> {
                val shouldShowRationale = (permissionState.status as PermissionStatus.Denied).shouldShowRationale
                Logger.d(
                    "Permission denied, should show rationale: $shouldShowRationale"
                )
                onPermissionDenied(shouldShowRationale)
            }
        }
    }
}
