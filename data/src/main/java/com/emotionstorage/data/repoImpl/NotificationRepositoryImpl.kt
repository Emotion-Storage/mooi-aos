package com.emotionstorage.data.repoImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.emotionstorage.data.dataSource.remote.NotificationRemoteDataSource
import com.emotionstorage.data.modelMapper.NotificationMapper
import com.emotionstorage.data.modelMapper.TimeCapsuleMapper
import com.emotionstorage.data.pagingSource.GetFavoriteTimeCapsulesPagingSource
import com.emotionstorage.data.pagingSource.GetNotificationsPagingSource
import com.emotionstorage.domain.model.Notification
import com.emotionstorage.domain.model.TimeCapsule
import com.emotionstorage.domain.repo.FavoriteSortBy
import com.emotionstorage.domain.repo.NotificationRepository
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private const val PAGE_SIZE = 20

class NotificationRepositoryImpl @Inject constructor(
    private val remoteDataSource: NotificationRemoteDataSource,
) : NotificationRepository {

    override fun getPagedNotifications(): Flow<PagingData<Notification>> {
        return Pager(
            config =
                PagingConfig(
                    pageSize = PAGE_SIZE,
                    enablePlaceholders = false,
                ),
            pagingSourceFactory =
                {
                    GetNotificationsPagingSource(
                        remoteDataSource = remoteDataSource,
                    )
                },
        ).flow.map {
            it.map { entity ->
                NotificationMapper.toDomain(entity)
            }
        }
    }
}
