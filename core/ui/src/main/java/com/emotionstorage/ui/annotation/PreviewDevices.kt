package com.emotionstorage.ui.annotation

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)

// 최소 대응
@Preview(
    name = "Android Small",
    showBackground = true,
    widthDp = 360,
    heightDp = 640
)
// 기준 디바이스
@Preview(
    name = "Android Normal",
    showBackground = true,
    widthDp = 411,
    heightDp = 891
)
// 큰 폰 대응
@Preview(
    name = "Android Large",
    showBackground = true,
    widthDp = 412,
    heightDp = 915
)
// Z Flip 대응
// Z Flip 펼친 크기 = Z Fold 접힌 크기와 거의 동일
@Preview(
    name = "Z Flip",
    showBackground = true,
    widthDp = 360,
    heightDp = 748
)
// Z Fold 대응
@Preview(
    name = "Z Fold",
    showBackground = true,
    widthDp = 673,
    heightDp = 841
)
// 태블릿 대응
@Preview(
    name = "Tablet",
    showBackground = true,
    widthDp = 800,
    heightDp = 1280
)
annotation class PreviewDevices
