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
    fun pagingSource(
        status: String,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
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

    @Query("DELETE FROM time_capsule")
    suspend fun clearAll()
}
