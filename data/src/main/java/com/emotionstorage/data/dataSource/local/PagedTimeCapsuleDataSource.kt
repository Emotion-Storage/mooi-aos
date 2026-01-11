package com.emotionstorage.data.dataSource.local

import androidx.paging.PagingData
import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.domain.repo.FavoriteSortBy
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface PagedTimeCapsuleDataSource {
    fun getPagedTimeCapsules(
        status: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): Flow<PagingData<TimeCapsuleEntity>>

    fun getPagedFavoriteTimeCapsules(
        sortBy: FavoriteSortBy,
    ): Flow<PagingData<TimeCapsuleEntity>>
}
