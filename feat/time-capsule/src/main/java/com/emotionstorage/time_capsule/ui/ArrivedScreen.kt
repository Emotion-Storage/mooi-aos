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
import com.emotionstorage.time_capsule.ui.model.ArrivedTimeCapsule
import com.emotionstorage.ui.component.TopAppBar
import com.emotionstorage.ui.theme.MooiTheme

@Composable
fun ArrivedScreen(
    modifier: Modifier = Modifier,
    navToBack: () -> Unit = {}
) {
    StatelessArrivedScreen(modifier = modifier, navToBack = navToBack)
}

@Composable
private fun StatelessArrivedScreen(
    modifier: Modifier = Modifier,
    arrivedTimeCapsules: List<ArrivedTimeCapsule> = emptyList(),
    navToBack: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MooiTheme.colorScheme.background),
        topBar = {
            TopAppBar(title = "도착한 타임캡슐", showBackButton = true, onBackClick = navToBack)
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
private fun ArrivedScreenPreview() {
    MooiTheme {
        StatelessArrivedScreen()
    }
}