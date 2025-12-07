package com.emotionstorage.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class MooiColorScheme(
    val primaryBlue500: Color = Color(0xFFAECBFA),
    val secondaryBlue700: Color = Color(0xFF849BEA),
    val tertiary: Color = Color(0xFFAFCBFA),
    val backgroundDefault: Color = Color(0xFF1C1A22),
    val backgroundElevated: Color = Color(0xFF100E14),
    val backgroundTinted: Color = Color(0xFF262736),
    val backgroundTintedBlue: Color = Color(0xFF4B5885),
    val error: Color = Color(0xFFF36868),
    val gray900: Color = Color(0xFF1C1C1C),
    val gray800: Color = Color(0xFF3C3C3C),
    val gray700: Color = Color(0xFF5B5B5B),
    val gray600: Color = Color(0xFF6E6E6E),
    val gray500: Color = Color(0xFF979797),
    val gray400: Color = Color(0xFFB7B7B7),
    val gray300: Color = Color(0xFFDADADA),
    val gray200: Color = Color(0xFFEAEAEA),
    val gray100: Color = Color(0xFFF3F3F3),
    val gray50: Color = Color(0xFFF9F9F9),
)
