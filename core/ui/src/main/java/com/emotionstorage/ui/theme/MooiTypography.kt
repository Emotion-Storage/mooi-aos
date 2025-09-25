package com.emotionstorage.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.emotionstorage.ui.R

val pretendard =
    FontFamily(
        Font(R.font.pretendard_black, weight = FontWeight.Black),
        Font(R.font.pretendard_bold, weight = FontWeight.Bold),
        Font(R.font.pretendard_extrabold, weight = FontWeight.ExtraBold),
        Font(R.font.pretendard_extralight, weight = FontWeight.ExtraLight),
        Font(R.font.pretendard_light, weight = FontWeight.Light),
        Font(R.font.pretendard_medium, weight = FontWeight.Medium),
        Font(R.font.pretendard_regular, weight = FontWeight.Normal),
        Font(R.font.pretendard_semibold, weight = FontWeight.SemiBold),
        Font(R.font.pretendard_thin, weight = FontWeight.Thin),
    )

@Immutable
data class MooiTypography(
    val head1: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            letterSpacing = (-0.02).em,
            lineHeight = (26 * 1.4).sp,
        ),
    val head2: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold,
            fontSize = 23.sp,
            letterSpacing = (-0.02).em,
            lineHeight = (23 * 1.4).sp,
        ),
    val head3: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 21.sp,
            letterSpacing = (-0.02).em,
            lineHeight = (21 * 1.4).sp,
        ),
    val body1: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            letterSpacing = (-0.02).em,
        ),
    val body2: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = (-0.02).em,
        ),
    val body3: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            letterSpacing = (-0.02).em,
        ),
    val body4: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = (-0.02).em,
        ),
    val body5: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 13.sp,
            letterSpacing = (-0.02).em,
        ),
    val body6: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            letterSpacing = (-0.02).em,
        ),
    val button: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            letterSpacing = (-0.02).em,
        ),
)
