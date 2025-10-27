package com.emotionstorage.data.repoImpl

import com.emotionstorage.data.dataSource.DailyReportRemoteDataSource
import com.emotionstorage.data.modelMapper.DailyReportMapper
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.DailyReport
import com.emotionstorage.domain.repo.DailyReportRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class DailyReportRepositoryImpl @Inject constructor(
    private val remoteDataSource: DailyReportRemoteDataSource,
) : DailyReportRepository {
    override suspend fun getDailyReport(date: LocalDate): Flow<DataState<DailyReport>> =
        flow {
            emit(DataState.Loading(isLoading = true))
            try {
                emit(
                    remoteDataSource.getDailyReport(date).run {
                        DataState.Success(
                            DailyReportMapper.toDomain(this),
                        )
                    },
                )
            } catch (e: Exception) {
                emit(DataState.Error(e))
            } finally {
                emit(DataState.Loading(isLoading = false))
            }
        }
}
