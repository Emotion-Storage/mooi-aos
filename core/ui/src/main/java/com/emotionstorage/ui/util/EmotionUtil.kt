package com.emotionstorage.ui.util

import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.ui.R

fun TimeCapsule.Emotion.getIconResId(): Int? =
    when (this.icon) {
        0 -> R.drawable.emotion_0
        1 -> R.drawable.emotion_1
        2 -> R.drawable.emotion_2
        3 -> R.drawable.emotion_3
        4 -> R.drawable.emotion_4
        else -> null
    }
