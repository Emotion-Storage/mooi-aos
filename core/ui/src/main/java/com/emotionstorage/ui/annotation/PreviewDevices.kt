package com.emotionstorage.ui.annotation

import androidx.compose.ui.tooling.preview.Preview

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@Preview(
    // 최소 대응
    name = "Android Small",
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
)
@Preview(
    // 기준 디바이스
    name = "Android Normal",
    showBackground = true,
    widthDp = 411,
    heightDp = 891,
)
@Preview(
    // 큰 폰 대응
    name = "Android Large",
    showBackground = true,
    widthDp = 412,
    heightDp = 915,
)
@Preview(
    // Z Flip 대응
    // Z Flip 펼친 크기 = Z Fold 접힌 크기와 거의 동일
    name = "Z Flip",
    showBackground = true,
    widthDp = 360,
    heightDp = 748,
)
@Preview(
    // Z Fold 대응
    name = "Z Fold",
    showBackground = true,
    widthDp = 673,
    heightDp = 841,
)
@Preview(
    // 태블릿 대응
    name = "Tablet",
    showBackground = true,
    widthDp = 800,
    heightDp = 1280,
)
annotation class PreviewDevices
