package com.emotionstorage.domain.useCase.home

import com.emotionstorage.domain.repo.AttendanceRepository
import javax.inject.Inject

class ClaimAttendanceRewardUseCase @Inject constructor(
    private val repo: AttendanceRepository,
) {
    operator fun invoke() = repo.claimAttendance()
}
