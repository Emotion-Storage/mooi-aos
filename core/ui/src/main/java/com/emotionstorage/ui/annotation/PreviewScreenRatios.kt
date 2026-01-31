package com.emotionstorage.ui.annotation

import androidx.compose.ui.tooling.preview.Preview

/**
 * 화면 비율 별 프리뷰
 * 1. 가장 대중적인 표준 비율 (9:19.5 ~ 9:20)
 * 2. 과거 표준이자 세로가 짧은 비율 (9:16)
 * 3. 매우 길고 좁은 비율 (Z Fold Cover / Z Flip 실물 급)
 * 4. 거의 정사각형 (Fold Main / Tablet Portrait)
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@Preview(name = "Ratio 9:20 (Standard)", widthDp = 412, heightDp = 915, showBackground = true)
@Preview(name = "Ratio 9:16 (Old/Small)", widthDp = 360, heightDp = 640, showBackground = true)
@Preview(name = "Ratio 9:23 (Long/Thin)", widthDp = 360, heightDp = 920, showBackground = true)
@Preview(name = "Ratio 5:6 (Square-ish)", widthDp = 775, heightDp = 931, showBackground = true)
@Preview(name = "Phone • MinWidth (320x700dp)", widthDp = 320, heightDp = 700, showSystemUi = true)
@Preview(name = "Phone • Mid (384x854dp)", widthDp = 384, heightDp = 854, showSystemUi = true)
@Preview(name = "Design Base (360x800dp)", widthDp = 360, heightDp = 800, showSystemUi = true)
annotation class PreviewScreenRatios
