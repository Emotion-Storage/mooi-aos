package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.remote.AttendanceRemoteDataSource
import com.emotionstorage.data.model.AttendanceEntity
import com.emotionstorage.remote.api.AttendanceApiService
import com.emotionstorage.remote.response.attendance.toEntity
import javax.inject.Inject

class AttendanceRemoteDataSourceImpl @Inject constructor(
    private val attendanceApi: AttendanceApiService,
) : AttendanceRemoteDataSource {
    override suspend fun getAttendanceStatus(): AttendanceEntity {
        try {
            val attendanceResponse = attendanceApi.getAttendanceStatus()
            if (attendanceResponse.data != null) {
                return attendanceResponse.data.toEntity()
            } else {
                throw Throwable("Response data is null!, $attendanceResponse")
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun requestAttendance(rewardDate: String): AttendanceEntity {
        try {
            val attendanceResponse = attendanceApi.postAttendance(rewardDate)
            if (attendanceResponse.data != null) {
                return attendanceResponse.data.toEntity()
            } else {
                throw Throwable("Response data is null!, $attendanceResponse")
            }
        } catch (e: Exception) {
            throw e
        }
    }
}
