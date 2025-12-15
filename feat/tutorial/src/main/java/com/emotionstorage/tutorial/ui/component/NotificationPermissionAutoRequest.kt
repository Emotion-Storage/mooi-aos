package com.emotionstorage.tutorial.ui.component

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.domain.model.NotificationPermissionStatus
import com.emotionstorage.presentation.notification.NotificationPermissionGateViewModel
import com.emotionstorage.ui.util.RequestPermission
import com.emotionstorage.ui.util.RequestPermissionEvent

@Composable
fun NotificationPermissionAutoRequest(
    onEvent: RequestPermissionEvent = RequestPermissionEvent.ON_START,
    viewModel: NotificationPermissionGateViewModel = hiltViewModel(),
) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

    val info by viewModel.info.collectAsState()

    if (info.status != NotificationPermissionStatus.Granted && info.hasPrompted.not()) {
        RequestPermission(
            permission = Manifest.permission.POST_NOTIFICATIONS,
            onEvent = onEvent,
            onPermissionGranted = { viewModel.onPermissionResult(true, showRationale = true) },
            onPermissionDenied = { showRationale ->
                viewModel.onPermissionResult(false, showRationale)
            },
        )
    }
}
