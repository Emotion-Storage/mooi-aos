package com.emotionstorage.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emotionstorage.local.room.AppDatabaseConstant

// only one session can exist in the database (= current session)
// set primary key to fixed value, 0
const val SESSION_PRIMARY_KEY = 0

@Entity(tableName = AppDatabaseConstant.TABLE_NAME.session)
data class SessionLocal(
    @PrimaryKey
    val pk: Int = SESSION_PRIMARY_KEY,
    val accessToken: String
)
