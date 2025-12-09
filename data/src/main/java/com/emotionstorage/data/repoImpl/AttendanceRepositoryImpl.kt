package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.remote.AttendanceRemoteDataSource
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.AttendanceSummary
import com.emotionstorage.domain.repo.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AttendanceRepositoryImpl @Inject constructor(
    private val remote: AttendanceRemoteDataSource,
) : AttendanceRepository {
    override fun getAttendanceSummary(): Flow<DataState<AttendanceSummary>> =
        flow {
            try {
                val entity = remote.getAttendanceStatus()
                val streak = entity.streak.toInt()
                val alreadyClaimedToday = entity.isAttendedToday

                val days = buildDays(streak, alreadyClaimedToday)
                val canClaimToday = !alreadyClaimedToday

                emit(DataState.Success(AttendanceSummary(days, canClaimToday)))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }

    override fun claimAttendance(): Flow<DataState<AttendanceSummary>> =
        flow {
            try {
                val today = LocalDate.now()
                val rewardDate = today.format(DateTimeFormatter.ISO_DATE)

                val entity = remote.requestAttendance(rewardDate)
                val streak = entity.streak.toInt()
                val alreadyClaimedToday = entity.isAttendedToday

                val days = buildDays(streak, alreadyClaimedToday)
                val canClaimToday = !alreadyClaimedToday

                emit(DataState.Success(AttendanceSummary(days, canClaimToday)))
            } catch (e: Exception) {
                emit(DataState.Error(e))
            }
        }

    private fun buildDays(
        streak: Int,
        alreadyClaimedToday: Boolean,
    ): List<AttendanceSummary.Attendance> {
        fun reward(day: Int) = if (day == 7) 3 else 1

        val cyclePosition = streak % 7
        val attendedCount =
            when {
                streak <= 0 -> 0
                alreadyClaimedToday && cyclePosition == 0 -> 7
                else -> cyclePosition
            }
        val nextClaimDay =
            if (alreadyClaimedToday) {
                null
            } else {
                val candidate = cyclePosition + 1
                if (candidate <= 0 || candidate > 7) 1 else candidate
            }

        return (1..7).map { day ->
            val status =
                when {
                    day <= attendedCount -> AttendanceSummary.AttendanceStatus.ATTENDED
                    nextClaimDay != null && day == nextClaimDay -> AttendanceSummary.AttendanceStatus.TODAY
                    else -> AttendanceSummary.AttendanceStatus.UPCOMING
                }
            AttendanceSummary.Attendance(day = day, rewardKeys = reward(day), status = status)
        }
    }
}
