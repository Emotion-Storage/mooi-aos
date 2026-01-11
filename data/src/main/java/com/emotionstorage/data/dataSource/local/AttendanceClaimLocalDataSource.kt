package com.emotionstorage.data.dataSource.local

interface AttendanceClaimLocalDataSource {
    suspend fun getLastClaimDate(): String?

    suspend fun setLastClaimDate(date: String)

    suspend fun clear()
}
