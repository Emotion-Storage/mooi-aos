package com.emotionstorage.home.ui.model

enum class AttendanceStatus { ATTENDED, TODAY, UPCOMING }

data class Attendance(
    val day: Int,
    val rewardKeys: Int,
    val status: AttendanceStatus,
)
