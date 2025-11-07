package com.emotionstorage.domain.repo

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.AttendanceSummary
import kotlinx.coroutines.flow.Flow

interface AttendanceRepository {
    fun getAttendanceSummary(): Flow<DataState<AttendanceSummary>>

    fun claimAttendance(): Flow<DataState<AttendanceSummary>>
}
