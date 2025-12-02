package com.emotionstorage.ui.component.toast

import android.view.Gravity
import android.view.WindowManager
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val SNACKBAR_VERTICAL_PADDING = 30

/**
 * custom data class for additional snack bar data
 */
data class CustomSnackbarData(
    val message: String,
    val iconResId: Int?,
)

class AppSnackbarController(
    private val snackbarHostState: SnackbarHostState,
) {
    private val _currentData = MutableStateFlow<CustomSnackbarData?>(null)
    val currentData = _currentData.asStateFlow()

    suspend fun showSnackbar(
        message: String,
        iconResId: Int? = null,
    ) {
        _currentData.emit(CustomSnackbarData(message, iconResId))
        snackbarHostState.showSnackbar(message)
    }
}

@Composable
fun AppSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    customDataFlow: StateFlow<CustomSnackbarData?>? = null,
    gravity: Int = Gravity.BOTTOM,
    paddingValues: PaddingValues = PaddingValues(vertical = SNACKBAR_VERTICAL_PADDING.dp),
    hostContent: @Composable (message: String, iconId: Int?) -> Unit = { message, iconId ->
        Toast(
            message = message,
            iconId = iconId,
        )
    },
) {
    val customData = customDataFlow?.collectAsState()

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
                        hostContent(it.visuals.message, customData?.value?.iconResId)
                    }
                }
            }
        }
}
