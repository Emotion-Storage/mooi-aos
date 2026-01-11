package com.emotionstorage.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.emotionstorage.local.room.database.AppDatabaseConstant

/**
 *  queryKey = "favorite-$sortBy"
 */
@Entity(
    tableName = AppDatabaseConstant.TableName.FAVORITE_TIME_CAPSULE_REMOTE_KEY_TABLE,
    indices = [
        Index("queryKey"),
    ],
)
data class FavoriteTimeCapsuleRemoteKeyLocal(
    @PrimaryKey
    val id: Long,
    val queryKey: String,
    val prevPage: Int?,
    val nextPage: Int?,
    val lastUpdated: Long,
)
