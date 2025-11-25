package com.emotionstorage.domain.repo

import androidx.paging.PagingData
import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.TimeCapsule
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth

interface TimeCapsuleRepository {
    suspend fun setTimeCapsuleOpenAt(
        id: Long,
        openAt: LocalDateTime,
    ): DataState<Unit>

    suspend fun openTimeCapsule(id: Long): Flow<DataState<Unit>>

    suspend fun saveTimeCapsuleNote(
        id: Long,
        note: String,
    ): Flow<DataState<Boolean>>

    suspend fun setFavoriteTimeCapsule(
        id: Long,
        isFavorite: Boolean,
    ): Flow<DataState<FavoriteResult>>

    fun getPagedFavoriteTimeCapsules(sortBy: FavoriteSortBy): Flow<PagingData<TimeCapsule>>

    fun getPagedTimeCapsules(
        startDate: LocalDate,
        endDate: LocalDate,
        status: String,
    ): Flow<PagingData<TimeCapsule>>

    suspend fun getTimeCapsuleById(id: Long): Flow<DataState<TimeCapsule>>

    suspend fun getTimeCapsuleDates(yearMonth: YearMonth): Flow<DataState<List<LocalDate>>>

    suspend fun deleteTimeCapsule(id: Long): Flow<DataState<Boolean>>

    suspend fun createTimeCapsule(id: Long): DataState<Long>
}

enum class FavoriteResult {
    ADDED,
    REMOVED,
    FULL,
}

enum class FavoriteSortBy(
    val label: String,
) {
    NEWEST("최신 날짜순"),
    FAVORITE_AT("즐겨찾기순"),
    ;

    companion object {
        fun getByLabel(label: String): FavoriteSortBy =
            FavoriteSortBy.entries.find { it.label == label }
                ?: throw IllegalArgumentException("Invalid sort order label: $label")
    }
}
