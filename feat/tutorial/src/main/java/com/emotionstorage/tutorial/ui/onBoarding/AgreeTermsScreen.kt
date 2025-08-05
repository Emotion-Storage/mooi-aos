package com.emotionstorage.tutorial.ui.onBoarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.emotionstorage.ui.theme.MooiTheme

/**
 * On boarding step 4
 * - agree terms of service
 * - signup user before navigating to signup complete screen
 */
@Composable
fun AgreeTermsScreen(
    modifier: Modifier = Modifier,
    navToSignupComplete: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier
            .background(MooiTheme.colorScheme.background)
            .fillMaxSize()
    ) { padding ->
        Column(modifier = modifier.padding(padding)) {
            Text("Agree Terms")
        }
    }
}