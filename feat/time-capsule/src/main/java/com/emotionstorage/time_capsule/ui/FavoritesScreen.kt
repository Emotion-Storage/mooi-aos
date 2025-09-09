package com.emotionstorage.time_capsule.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.emotionstorage.time_capsule.ui.model.FavoriteTimeCapsule
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme


@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    navToBack: () -> Unit = {}
) {
    StatelessFavoriteScreen(modifier = modifier, navToBack = navToBack)
}

@Composable
private fun StatelessFavoriteScreen(
    modifier: Modifier = Modifier,
    favoriteTimeCapsules: List<FavoriteTimeCapsule> = emptyList(),
    navToBack: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(title = "내 마음 서랍", showBackButton = true, onBackClick = navToBack)
        }
    ) { innerPadding ->
            LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MooiTheme.colorScheme.background)
                .padding(innerPadding)
                .scrollable(scrollState, orientation = Orientation.Vertical)
        ) {

        }
    }
}

@Preview
@Composable
private fun FavoriteScreenPreview() {
    MooiTheme {
        StatelessFavoriteScreen()
    }
}