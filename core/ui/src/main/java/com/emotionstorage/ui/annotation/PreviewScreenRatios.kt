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
@Preview(name = "Old Galaxy S Series(360x740)", widthDp = 360, heightDp = 740, showBackground = true)
@Preview(name = "Galaxy S Ultra Series(412x915)", widthDp = 412, heightDp = 915, showBackground = true)
@Preview(name = "Galaxy note 10, 10+, s10, s10 5g", widthDp = 412, heightDp = 869, showBackground = true)
@Preview(name = "Galaxy S Ultra Series(384x824)", widthDp = 384, heightDp = 824, showBackground = true)
@Preview(name = "Galaxy S+ Series (384x854)", widthDp = 384, heightDp = 854, showBackground = true)
@Preview(name = "Galaxy S+ Series (384x832)", widthDp = 384, heightDp = 832, showBackground = true)
@Preview(name = "Design Base + Galaxy S Series (360x800dp)", widthDp = 360, heightDp = 800, showBackground = true)
@Preview(name = "Galaxy S Series(360x780)", widthDp = 360, heightDp = 780, showBackground = true)
annotation class PreviewScreenRatios
