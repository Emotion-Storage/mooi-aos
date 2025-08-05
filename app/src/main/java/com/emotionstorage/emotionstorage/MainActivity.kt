package com.emotionstorage.emotionstorage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.emotionstorage.ui.theme.MooiTheme
import com.emotionstorage.tutorial.ui.TutorialScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MooiTheme {
                TutorialScreen()
            }
        }
    }
}