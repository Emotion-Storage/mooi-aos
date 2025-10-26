package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.DailyReportRemoteDataSource
import com.emotionstorage.data.model.DailyReportEntity
import com.emotionstorage.remote.api.DailyReportApiService
import com.emotionstorage.remote.modelMapper.DailyReportResponseMapper
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DailyReportRemoteDataSourceImpl @Inject constructor(
    private val apiService: DailyReportApiService,
) : DailyReportRemoteDataSource {
    override suspend fun getDailyReport(date: LocalDate): DailyReportEntity {
        try {
            val response =
                apiService.getDailyReport(
                    date = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                )
            if (response.data != null)
                {
                    return DailyReportResponseMapper.toData(response.data!!)
                } else
                {
                    throw Exception("getDailyReport response data is empty, $response")
                }
        } catch (e: Exception) {
            throw Exception("getDailyReport api fail, $e")
        }
    }
}
