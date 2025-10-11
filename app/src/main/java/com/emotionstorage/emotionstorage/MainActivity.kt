package com.emotionstorage.emotionstorage

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.emotionstorage.emotionstorage.ui.AppNavHost
import dagger.hilt.android.AndroidEntryPoint
import com.emotionstorage.ui.theme.MooiTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // set system bar style to dark mode
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
        setContent {
            MooiTheme {
                AppNavHost()
            }
        }
    }
}
