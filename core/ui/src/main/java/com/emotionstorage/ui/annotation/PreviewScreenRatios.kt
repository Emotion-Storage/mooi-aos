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
@Preview(name = "Old Galaxy S Series(360x692)", widthDp = 360, heightDp = 692, showBackground = true)
@Preview(name = "Galaxy S Ultra Series(412x867)", widthDp = 412, heightDp = 867, showBackground = true)
@Preview(name = "Galaxy note 10, 10+, s10, s10 5g(412x821)", widthDp = 412, heightDp = 821, showBackground = true)
@Preview(name = "Galaxy S Ultra Series(384x776)", widthDp = 384, heightDp = 776, showBackground = true)
@Preview(name = "Galaxy S+ Series (384x806)", widthDp = 384, heightDp = 806, showBackground = true)
@Preview(name = "Galaxy S+ Series (384x784)", widthDp = 384, heightDp = 784, showBackground = true)
@Preview(name = "Design Base + Galaxy S Series (360x752dp)", widthDp = 360, heightDp = 752, showBackground = true)
@Preview(name = "Galaxy S Series(360x732)", widthDp = 360, heightDp = 732, showBackground = true)
annotation class PreviewScreenRatios
