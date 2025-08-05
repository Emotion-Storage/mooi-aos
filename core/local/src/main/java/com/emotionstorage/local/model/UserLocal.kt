package com.emotionstorage.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emotionstorage.local.room.AppDatabaseConstant
import java.time.LocalDateTime

// only one user can exist in the database (= currently logged in user)
// set primary key to fixed value, 0
const val USER_PRIMARY_KEY = 0

@Entity(tableName = AppDatabaseConstant.TABLE_NAME.user)
data class UserLocal(
    @PrimaryKey
    val pk: Int = USER_PRIMARY_KEY,

    val id: String,
    val socialType: String,
    val socialId: String,

    val email: String,
    val name: String,
    val profileImageUrl: String? = null,

    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)