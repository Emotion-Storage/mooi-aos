package com.emotionstorage.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emotionstorage.local.model.TimeCapsuleRemoteKeyLocal

@Dao
interface TimeCapsuleRemoteKeyDao {

    @Query(
        """
        SELECT * FROM time_capsule_remote_key
        WHERE id = :id AND queryKey = :queryKey
    """
    )
    suspend fun remoteKeyById(
        id: Long,
        queryKey: String,
    ): TimeCapsuleRemoteKeyLocal?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<TimeCapsuleRemoteKeyLocal>)

    @Query("DELETE FROM time_capsule_remote_key WHERE queryKey = :queryKey")
    suspend fun clearByQueryKey(queryKey: String)

    @Query(
        """
        SELECT MAX(lastUpdated)
        FROM time_capsule_remote_key
        WHERE queryKey = :queryKey
    """
    )
    suspend fun lastUpdated(queryKey: String): Long?
}
