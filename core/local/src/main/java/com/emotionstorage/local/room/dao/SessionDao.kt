package com.emotionstorage.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.emotionstorage.local.model.SESSION_PRIMARY_KEY
import com.emotionstorage.local.model.SessionLocal

@Dao
interface SessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: SessionLocal)

    @Query("SELECT * FROM session WHERE pk = $SESSION_PRIMARY_KEY")
    suspend fun getSession(): SessionLocal?

    @Query("DELETE FROM session WHERE pk = $SESSION_PRIMARY_KEY")
    suspend fun deleteSession()
}
