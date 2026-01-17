package com.emotionstorage.data.dataSource.remote

import com.emotionstorage.data.model.TimeCapsuleEntity
import com.emotionstorage.domain.common.DataState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

interface TimeCapsuleRemoteDataSource {
    suspend fun getTimeCapsules(
        startDate: LocalDate,
        endDate: LocalDate,
        page: Int,
        limit: Int,
        status: String,
    ): List<TimeCapsuleEntity>

    suspend fun getTimeCapsuleDates(yearMonth: YearMonth): List<LocalDate>

    suspend fun getNewTimeCapsuleCount(): Int

    suspend fun getFavoriteTimeCapsules(
        page: Int,
        limit: Int,
        sortBy: String,
    ): List<TimeCapsuleEntity>

    suspend fun getTimeCapsuleDetail(id: Long): TimeCapsuleEntity

    suspend fun patchTimeCapsuleOpen(id: Long): Boolean

    suspend fun postTimeCapsuleOpenAt(
        id: Long,
        openAt: LocalDateTime,
    ): Boolean

    suspend fun patchTimeCapsuleNote(
        id: Long,
        note: String,
    ): Boolean

    suspend fun patchTimeCapsuleFavorite(
        id: Long,
        isFavorite: Boolean,
    ): DataState<Boolean>

    suspend fun deleteTimeCapsule(id: Long): Boolean

    suspend fun createTimeCapsule(id: Long): Long
}
