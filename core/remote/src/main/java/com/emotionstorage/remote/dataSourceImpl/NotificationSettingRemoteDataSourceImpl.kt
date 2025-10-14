package com.emotionstorage.remote.datasourceImpl

import com.emotionstorage.data.dataSource.NotificationSettingRemoteDataSource
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.NotificationSettings
import com.emotionstorage.remote.api.MyPageApiService
import com.emotionstorage.remote.request.myPage.toBody
import com.emotionstorage.remote.request.myPage.toRequest
import com.emotionstorage.remote.response.myPage.toDomain
import com.emotionstorage.remote.response.toDataState
import com.emotionstorage.remote.response.toEmptyDataState
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class NotificationSettingRemoteDataSourceImpl @Inject constructor(
    private val myPageApiService: MyPageApiService,
) : NotificationSettingRemoteDataSource {
    override suspend fun getNotificationSettings(): DataState<NotificationSettings> =
        try {
            myPageApiService.getNotificationSettings().toDataState { dto ->
                dto.toDomain()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            DataState.Error(e)
        }

    override suspend fun updateNotificationSettings(notificationSettings: NotificationSettings): DataState<Unit> =
        try {
            val dto = myPageApiService.updateNotificationSettings(notificationSettings.toRequest().toBody())
            dto.toEmptyDataState()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            DataState.Error(e)
        }
}
