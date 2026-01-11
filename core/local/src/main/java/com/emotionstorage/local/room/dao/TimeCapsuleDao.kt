package com.emotionstorage.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.emotionstorage.local.model.TimeCapsuleLocal
import java.time.LocalDateTime

@Dao
interface TimeCapsuleDao {
    @Upsert
    suspend fun upsertAll(timeCapsules: List<TimeCapsuleLocal>)

    @Query(
        """
        SELECT * FROM time_capsule
        WHERE status = :status
          AND historyDate BETWEEN :startDate AND :endDate
        ORDER BY historyDate DESC
    """,
    )
    fun favoritePagingSource(
        status: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): PagingSource<Int, TimeCapsuleLocal>

    @Query(
        """
        SELECT * FROM time_capsule
        WHERE isFavorite = 1
        ORDER BY
            CASE WHEN :sortBy = 'createdAt' THEN createdAt END DESC,
            CASE WHEN :sortBy = 'favoriteAt' THEN favoriteAt END DESC
    """
    )
    fun favoritePagingSource(
        sortBy: String
    ): PagingSource<Int, TimeCapsuleLocal>


    @Query(
        """
        DELETE FROM time_capsule
        WHERE status = :status
          AND historyDate BETWEEN :startDate AND :endDate
    """,
    )
    suspend fun clearByCondition(
        status: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    )

    @Query(
        """
        DELETE FROM time_capsule
        WHERE isFavorite = 1
        """
    )
    suspend fun clearFavorites()

    @Query("DELETE FROM time_capsule")
    suspend fun clearAll()
}
