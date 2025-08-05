package com.emotionstorage.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emotionstorage.local.model.USER_PRIMARY_KEY
import com.emotionstorage.local.model.UserLocal


@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserLocal)

    @Query("SELECT * FROM user WHERE pk = $USER_PRIMARY_KEY")
    suspend fun getUser(): UserLocal?

    @Query("DELETE FROM user WHERE pk = $USER_PRIMARY_KEY")
    suspend fun deleteUser()
}


