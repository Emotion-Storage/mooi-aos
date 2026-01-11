package com.emotionstorage.local.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.emotionstorage.local.room.database.AppDatabaseConstant

/**
 *  queryKey = "$status-$startDate-$endDate"
 */
@Entity(
    tableName = AppDatabaseConstant.TableName.TIME_CAPSULE_REMOTE_KEY_TABLE,
    indices = [
        Index("queryKey"),
    ],
)
data class TimeCapsuleRemoteKey(
    @PrimaryKey
    val id: Long,
    val queryKey: String,
    val prevPage: Int?,
    val nextPage: Int?,
    val lastUpdated: Long,
)
