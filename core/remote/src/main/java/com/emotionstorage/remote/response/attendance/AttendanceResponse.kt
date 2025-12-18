package com.emotionstorage.remote.response.attendance

import com.emotionstorage.data.model.AttendanceEntity
import kotlinx.serialization.Serializable

@Serializable
data class AttendanceResponse(
    val streak: Long,
    val isAttendedToday: Boolean,
)

fun AttendanceResponse.toEntity(): AttendanceEntity =
    AttendanceEntity(
        streak = streak,
        isAttendedToday = isAttendedToday,
    )
