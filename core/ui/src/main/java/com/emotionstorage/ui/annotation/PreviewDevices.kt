package com.emotionstorage.ui.annotation

import androidx.compose.ui.tooling.preview.Devices.PIXEL_4
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
@Preview(
    name = "Android Small",
    showBackground = true,
    widthDp = 320,
    heightDp = 480,
)
@Preview(
    name = "Android Normal",
    showBackground = true,
    device = PIXEL_4,
)
@Preview(
    name = "Android Large",
    showBackground = true,
    device = PIXEL_7_PRO,
)
@Preview(
    name = "Z Flip 5 - Open",
    widthDp = 360,
    heightDp = 748,
)
@Preview(
    name = "Z Fold 5 - Open",
    showBackground = true,
    widthDp = 673,
    heightDp = 841,
)
annotation class PreviewDevices
