package com.emotionstorage.data.dataSource.remote

import com.emotionstorage.data.model.AttendanceEntity

interface AttendanceRemoteDataSource {
    suspend fun getAttendanceStatus(): AttendanceEntity

    suspend fun requestAttendance(rewardDate: String): AttendanceEntity
}
