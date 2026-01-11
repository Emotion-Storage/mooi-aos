package com.emotionstorage.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emotionstorage.local.model.FavoriteTimeCapsuleRemoteKeyLocal

@Dao
interface FavoriteTimeCapsuleRemoteKeyDao {
    @Query(
        """
        SELECT * FROM favorite_time_capsule_remote_key
        WHERE id = :id AND queryKey = :queryKey
    """,
    )
    suspend fun remoteKeyById(
        id: Long,
        queryKey: String,
    ): FavoriteTimeCapsuleRemoteKeyLocal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<FavoriteTimeCapsuleRemoteKeyLocal>)

    @Query("DELETE FROM favorite_time_capsule_remote_key WHERE queryKey = :queryKey")
    suspend fun clearByQueryKey(queryKey: String)

    @Query(
        """
        SELECT MAX(lastUpdated)
        FROM favorite_time_capsule_remote_key
        WHERE queryKey = :queryKey
    """,
    )
    suspend fun lastUpdated(queryKey: String): Long?
}
