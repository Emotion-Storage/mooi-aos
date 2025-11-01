package com.emotionstorage.ui.component.toast

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emotionstorage.domain.repo.FavoriteResult
import com.emotionstorage.ui.R
import com.emotionstorage.ui.theme.MooiTheme
import com.orhanobut.logger.Logger

@Composable
fun FavoriteToast(favoriteResultVal: String) {
    var favoriteResult: FavoriteResult? = null
    try {
        favoriteResult = FavoriteResult.valueOf(favoriteResultVal)
    } catch (e: IllegalArgumentException) {
        Logger.e("Invalid favorite result: $favoriteResultVal")
    }

    if (favoriteResult != null) {
        Toast(
            message =
                LocalContext.current.getString(
                    when (favoriteResult) {
                        FavoriteResult.ADDED -> R.string.toast_favorite_added
                        FavoriteResult.REMOVED -> R.string.toast_favorite_removed
                        FavoriteResult.FULL -> R.string.toast_favorite_full
                    },
                ),
            iconId =
                if (favoriteResult != FavoriteResult.FULL) {
                    R.drawable.success_filled
                } else {
                    null
                },
        )
    }
}

@Preview
@Composable
private fun FavoriteToastPreview() {
    MooiTheme {
        Column(
            modifier =
                Modifier
                    .background(MooiTheme.colorScheme.background)
                    .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            FavoriteToast(FavoriteResult.ADDED.name)
            FavoriteToast(FavoriteResult.FULL.name)
            FavoriteToast("INVALID_VALUE")
        }
    }
}
