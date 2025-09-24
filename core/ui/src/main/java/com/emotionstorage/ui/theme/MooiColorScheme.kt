package com.emotionstorage.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class MooiColorScheme(
    val primary: Color = Color(0xFFAFCBFA),
    val secondary: Color = Color(0xFF859CEA),
    val tertiary: Color = Color(0xFF859CEA),
    val background: Color = Color(0xFF1E1C24),
    val bottomBarBackground: Color = Color(0xFF100E14),
    val errorRed: Color = Color(0xFFF36969),
    val gray900: Color = Color(0xFF1E1E1E),
    val gray800: Color = Color(0xFF3D3D3D),
    val gray700: Color = Color(0xFF5C5C5C),
    val gray600: Color = Color(0xFF6F6F6F),
    val gray500: Color = Color(0xFF989898),
    val gray400: Color = Color(0xFFB7B8B8),
    val gray300: Color = Color(0xFFDADADA),
    val gray200: Color = Color(0xFFEAEAEA),
    val gray100: Color = Color(0xFFF3F3F3),
    val gray50: Color = Color(0xFFF9F9F9),
)
