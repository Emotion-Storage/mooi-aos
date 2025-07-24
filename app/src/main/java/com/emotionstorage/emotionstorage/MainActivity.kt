package com.emotionstorage.emotionstorage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.emotionstorage.emotionstorage.ui.theme.EmotionStorageTheme
import com.emotionstorage.tutorial.ui.LoginRoute

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmotionStorageTheme {
                LoginRoute(
                    navToHome = {},
                    navToOnboarding = {},
                    )
            }
        }
    }
}