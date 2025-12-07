package com.emotionstorage.time_capsule.ui.component.timeCapsuleItem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.time_capsule.ui.model.TimeCapsuleItemState
import com.emotionstorage.time_capsule.ui.util.TimeCapsuleItemStateProvider
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun TimeCapsuleItem(
    timeCapsule: TimeCapsuleItemState,
    modifier: Modifier = Modifier,
    showHeader: Boolean = true,
    onFavoriteClick: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    when (timeCapsule.status) {
        TimeCapsule.Status.TEMPORARY -> {
            TempTimeCapsuleItem(
                timeCapsule = timeCapsule,
                modifier = modifier,
                showHeader = showHeader,
                onClick = onClick,
            )
        }

        TimeCapsule.Status.LOCKED -> {
            LockedTimeCapsuleItem(
                timeCapsule = timeCapsule,
                modifier = modifier,
                showHeader = showHeader,
                onClick = onClick,
            )
        }

        TimeCapsule.Status.ARRIVED -> {
            ArrivedTimeCapsuleItem(
                timeCapsule = timeCapsule,
                modifier = modifier,
                showHeader = showHeader,
                onClick = onClick,
            )
        }

        TimeCapsule.Status.OPENED -> {
            OpenTimeCapsuleItem(
                timeCapsule = timeCapsule,
                modifier = modifier,
                showHeader = showHeader,
                onClick = onClick,
                onFavoriteClick = onFavoriteClick,
            )
        }
    }
}

@Preview
@Composable
private fun TimeCapsuleItemPreview(
    @PreviewParameter(TimeCapsuleItemStateProvider::class) timeCapsule: TimeCapsuleItemState,
) {
    MooiTheme {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MooiTheme.colorScheme.background)
                    .padding(16.dp),
        ) {
            TimeCapsuleItem(
                timeCapsule = timeCapsule,
            )
        }
    }
}
