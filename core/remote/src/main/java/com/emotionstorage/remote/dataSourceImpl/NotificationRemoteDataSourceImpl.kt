package com.emotionstorage.remote.dataSourceImpl

import com.emotionstorage.data.dataSource.remote.NotificationRemoteDataSource
import com.emotionstorage.data.model.NotificationEntity
import com.emotionstorage.remote.api.NotificationApiService
import com.emotionstorage.remote.modelMapper.NotificationResponseMapper
import javax.inject.Inject

class NotificationRemoteDataSourceImpl @Inject constructor(
    private val apiService: NotificationApiService
) : NotificationRemoteDataSource {

    override suspend fun getNotifications(
        page: Int,
        limit: Int
    ): List<NotificationEntity> = try {
        val response = apiService.getNotifications(page, limit)
        response.data?.let{
            NotificationResponseMapper.toData(it)
        } ?: throw Exception("getNotifications response data is empty, $response")
    } catch (e: Exception) {
        throw Exception("getNotifications api fail, ${e.message}", e)
    }
}
