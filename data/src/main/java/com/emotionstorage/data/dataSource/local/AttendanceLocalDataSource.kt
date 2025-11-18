package com.emotionstorage.data.dataSource.local

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface AttendanceLocalDataSource {
    fun wasDialogShown(date: LocalDate): Flow<Boolean>

    suspend fun setDialogShown(date: LocalDate)

    fun streak(): Flow<Int>

    fun lastClaimDate(): Flow<String?>
}
