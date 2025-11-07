package com.emotionstorage.home.data.repoImpl

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.AttendanceSummary
import com.emotionstorage.domain.repo.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    // TODO : api 연동
) : AttendanceRepository {
    override fun getAttendanceSummary(): Flow<DataState<AttendanceSummary>> = flow {
        // TODO : 출석 보상 상태 받아오기
    }

    override fun claimAttendance(): Flow<DataState<AttendanceSummary>> = flow {
        // TODO : 출석 보상 요청
    }
}
