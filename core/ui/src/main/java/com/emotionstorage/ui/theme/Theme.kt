package com.emotionstorage.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalMooiColorScheme = staticCompositionLocalOf {
    MooiColorScheme()
}
private val LocalMooiBrushScheme = staticCompositionLocalOf {
    MooiBrushScheme()
}
private val LocalMooiTypography = staticCompositionLocalOf {
    MooiTypography()
}

@Composable
fun MooiTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalMooiTypography provides MooiTypography()
    ) {
        MaterialTheme(
            content = content
        )
    }
}

object MooiTheme {
    val colorScheme: MooiColorScheme
        @Composable
        get() = LocalMooiColorScheme.current

    val brushScheme: MooiBrushScheme
        @Composable
        get() = LocalMooiBrushScheme.current

    val typography: MooiTypography
        @Composable
        get() = LocalMooiTypography.current
}
