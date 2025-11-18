package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.remote.DailyReportRemoteDataSource
import com.emotionstorage.data.modelMapper.DailyReportMapper
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.DailyReport
import com.emotionstorage.domain.repo.DailyReportRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class DailyReportRepositoryImpl @Inject constructor(
    private val remoteDataSource: DailyReportRemoteDataSource,
) : DailyReportRepository {
    override suspend fun getDailyReport(date: LocalDate): DataState<DailyReport> =
        try {
            remoteDataSource.getDailyReport(date).run {
                DataState.Success(
                    DailyReportMapper.toDomain(this),
                )
            }
        } catch (e: Exception) {
            DataState.Error(e)
        }


    override suspend fun getDailyReport(id: Long): DataState<DailyReport> =
        try {
            remoteDataSource.getDailyReport(id).run {
                DataState.Success(
                    DailyReportMapper.toDomain(this),
                )
            }
        } catch (e: Exception) {
            DataState.Error(e)
        }

    override suspend fun openDailyReport(id: Long): Boolean {
        try{
            return remoteDataSource.openDailyReport(id)
        } catch (e: Exception) {
            Napier.e("DailyReportRepositoryImpl: openDailyReport error: $e")
            return false
        }
    }
}
