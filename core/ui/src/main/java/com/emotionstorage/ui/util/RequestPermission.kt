package com.emotionstorage.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.LifecycleStartEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.orhanobut.logger.Logger

enum class RequestPermissionEvent {
    ON_RESUME, ON_START
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermission(
    permission: String,
    onEvent: RequestPermissionEvent = RequestPermissionEvent.ON_START,
    onPermissionGranted: () -> Unit = {},
    onPermissionDenied: (showRationale: Boolean) -> Unit = {}
) {
    val permissionState =
        rememberPermissionState(permission = permission)

    when (onEvent) {
        RequestPermissionEvent.ON_RESUME -> {
            LifecycleResumeEffect("onResume") {
                permissionState.launchPermissionRequest()
                onPauseOrDispose {}
            }
        }

        RequestPermissionEvent.ON_START -> {
            LifecycleStartEffect("onStart") {
                permissionState.launchPermissionRequest()
                onStopOrDispose {}
            }
        }
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
