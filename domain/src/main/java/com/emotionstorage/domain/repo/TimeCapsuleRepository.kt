package com.emotionstorage.domain.repo

import com.emotionstorage.domain.common.DataState
import com.emotionstorage.domain.model.TimeCapsule
import kotlinx.coroutines.flow.Flow

interface TimeCapsuleRepository {
    suspend fun openArrivedTimeCapsule(id: String): Flow<DataState<Unit>>

    suspend fun saveTimeCapsuleNote(
        id: String,
        note: String,
    ): Flow<DataState<Boolean>>

    suspend fun getFavoriteTimeCapsules(sortBy: FavoriteSortBy): Flow<DataState<List<TimeCapsule>>>
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
