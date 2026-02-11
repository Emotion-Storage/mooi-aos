package com.emotionstorage.emotionstorage

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.lifecycleScope
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
                Box(modifier = Modifier.fillMaxSize()) {
                    if (BuildConfig.DEBUG) {
                        DebugTag()
                    }
                    AppNavHost(
                        modifier = Modifier.fillMaxSize(),
                        getGoogleIdToken = {
                            GoogleCredentialManager(
                                activity = this@MainActivity,
                                coroutineScope = lifecycleScope,
                            ).getIdToken()
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun BoxScope.DebugTag(){
    Box(
        modifier = Modifier
            .align(Alignment.TopCenter)
            .zIndex(100f)
            .background(MooiTheme.colorScheme.error)
            .padding(2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Debug",
            color = androidx.compose.ui.graphics.Color.White,
        )
    }
}
