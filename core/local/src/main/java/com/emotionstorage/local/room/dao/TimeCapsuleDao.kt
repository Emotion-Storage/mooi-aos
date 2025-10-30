package com.emotionstorage.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.emotionstorage.local.model.TimeCapsuleLocal

@Dao
interface TimeCapsuleDao {
    @Upsert
    suspend fun upsertAll(timeCapsules: List<TimeCapsuleLocal>)

    @Query("SELECT * FROM time_capsule")
    fun pagingSource(): PagingSource<Int, TimeCapsuleLocal>

    @Query("DELETE FROM time_capsule")
    suspend fun clearAll()
}
