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
            fontWeight = FontWeight.SemiBold,
            fontSize = 26.sp,
            letterSpacing = (-0.02).em,
            lineHeight = (26 * 1.4).sp,
        ),
    val head2: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            letterSpacing = (-0.02).em,
            lineHeight = (26 * 1.4).sp,
        ),
    val head3: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 21.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val body1: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val body2: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val body3: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Light,
            fontSize = 17.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val body4: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val body5: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val body6: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val body7: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val body8: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val caption1: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val caption2: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val caption3: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val caption4: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val caption5: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val caption6: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val caption7: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Light,
            fontSize = 13.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val bottomBar: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val mainButton: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
    val error: TextStyle =
        TextStyle(
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            fontSize = 13.sp,
            letterSpacing = (-0.02).em,
            lineHeight = 24.sp,
        ),
)
