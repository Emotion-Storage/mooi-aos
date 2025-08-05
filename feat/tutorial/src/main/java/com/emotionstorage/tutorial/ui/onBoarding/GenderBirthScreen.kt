package com.emotionstorage.tutorial.ui.onBoarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.emotionstorage.tutorial.presentation.onBoarding.GenderBirthEvent
import com.emotionstorage.tutorial.presentation.onBoarding.GenderBirthViewModel
import com.emotionstorage.tutorial.presentation.onBoarding.GenderBirthViewModel.State
import com.emotionstorage.ui.theme.MooiTheme

/**
 * On boarding step 2
 * - input user gender
 * - input user birth date
 */
@Composable
fun GenderBirthScreen(
    modifier: Modifier = Modifier,
    viewModel: GenderBirthViewModel = hiltViewModel(),
    navToExpectations: () -> Unit = {},
) {
    val state = viewModel.state.collectAsState().value

    StatelessGenderBirthScreen(
        event = viewModel.event,
        state = state,
        modifier = modifier,
        navToExpectations = navToExpectations
    )
}

@Composable
private fun StatelessGenderBirthScreen(
    event: GenderBirthEvent,
    state: State,
    modifier: Modifier = Modifier,
    navToExpectations: () -> Unit = {},
){
    Scaffold(
        modifier = modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize()
    ) { padding ->
        Column(modifier = modifier.padding(padding)) {
            Text("Input Gender")
            Text("Input BirthYear")
            Text("Input BirthMonth")
            Text("Input BirthDay")
        }
    }
}
