package com.emotionstorage.emotionstorage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.emotionstorage.auth.ui.LoginScreen
import com.emotionstorage.tutorial.ui.onBoarding.OnBoardingNavHost
import dagger.hilt.android.AndroidEntryPoint
import com.emotionstorage.ui.theme.MooiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MooiTheme {
                OnBoardingNavHost()
            }
        }
    }
}