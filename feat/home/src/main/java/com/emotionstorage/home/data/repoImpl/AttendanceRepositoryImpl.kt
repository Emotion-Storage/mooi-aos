package com.emotionstorage.home.data.repoImpl

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.AttendanceSummary
import com.emotionstorage.domain.repo.AttendanceRepository
import com.emotionstorage.home.local.dataSourceImpl.AttendanceLocalDataSourceImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    // TODO : 테스트를 위한 구현체 주입 (추후 인터페이스 주입으로 변경)
    private val local: AttendanceLocalDataSourceImpl,
    // TODO : api 연동
) : AttendanceRepository {
    override fun getAttendanceSummary(): Flow<DataState<AttendanceSummary>> =
        flow {
            val today = LocalDate.now()
            val lastDate = local.lastClaimDate().first()?.let(LocalDate::parse)
            val streak = local.streak().first()

            val alreadyClaimedToday = (lastDate == today)
            val currentStreak = if (alreadyClaimedToday) streak else streak // 값 그대론데 가독성용

            val days = buildDays(currentStreak, alreadyClaimedToday)
            val canClaimToday = !alreadyClaimedToday

            emit(DataState.Success(AttendanceSummary(days = days, canClaimToday = canClaimToday)))
        }

    override fun claimAttendance(): Flow<DataState<AttendanceSummary>> =
        flow {
            val today = LocalDate.now()
            local.saveClaimToday(today)
            emitAll(getAttendanceSummary())
        }

    private fun buildDays(
        streak: Int,
        alreadyClaimedToday: Boolean,
    ): List<AttendanceSummary.Attendance> {
        fun reward(day: Int) = if (day == 7) 3 else 1

        val nextDay = if (streak == 0) 1 else (streak % 7) + if (alreadyClaimedToday) 0 else 1
        return (1..7).map { day ->
            val status =
                when {
                    day < nextDay -> AttendanceSummary.AttendanceStatus.ATTENDED
                    day == nextDay ->
                        if (alreadyClaimedToday) {
                            AttendanceSummary.AttendanceStatus.ATTENDED
                        } else {
                            AttendanceSummary.AttendanceStatus.TODAY
                        }

                    else -> AttendanceSummary.AttendanceStatus.UPCOMING
                }
            AttendanceSummary.Attendance(day = day, rewardKeys = reward(day), status = status)
        }
    }
}
