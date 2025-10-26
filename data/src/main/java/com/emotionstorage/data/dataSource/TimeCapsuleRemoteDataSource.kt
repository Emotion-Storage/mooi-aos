package com.emotionstorage.data.dataSource

import com.emotionstorage.data.model.TimeCapsuleEntity
import java.time.LocalDate
import java.time.YearMonth

interface TimeCapsuleRemoteDataSource {
    suspend fun patchTimeCapsuleOpen(id: Long): Boolean

    suspend fun patchTimeCapsuleNote(
        id: Long,
        note: String,
    ): Boolean

    suspend fun patchTimeCapsuleFavorite(
        id: Long,
        isFavorite: Boolean,
    ): Boolean

    suspend fun getFavoriteTimeCapsules(
        page: Int,
        limit: Int,
        sortBy: String,
    ): List<TimeCapsuleEntity>

    suspend fun getTimeCapsuleDetail(id: Long): TimeCapsuleEntity

    suspend fun getTimeCapsules(
        startDate: LocalDate,
        endDate: LocalDate,
        page: Int,
        limit: Int,
        status: String,
    ): List<TimeCapsuleEntity>

    suspend fun getTimeCapsuleDates(yearMonth: YearMonth): List<LocalDate>

    suspend fun deleteTimeCapsule(id: Long): Boolean
}
