package com.emotionstorage.domain.model

data class AttendanceSummary(
    val days: List<Attendance>,
    val canClaimToday: Boolean,
) {
    data class Attendance(
        val day: Int,
        val rewardKeys: Int,
        val status: AttendanceStatus,
    )

    enum class AttendanceStatus { ATTENDED, TODAY, UPCOMING }
}
