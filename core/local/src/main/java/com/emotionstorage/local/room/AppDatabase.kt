package com.emotionstorage.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.emotionstorage.local.model.SessionLocal
import com.emotionstorage.local.model.UserLocal
import com.emotionstorage.local.room.dao.SessionDao
import com.emotionstorage.local.room.dao.UserDao

object AppDatabaseConstant {
    const val NAME = "mooi-room-database"
    const val VERSION = 2

    object TABLE_NAME {
        const val user = "user"
        const val session = "session"
    }
}

@Database(
    entities = [UserLocal::class, SessionLocal::class],
    version = AppDatabaseConstant.VERSION
)
@TypeConverters(
    DtoConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun sessionDao(): SessionDao
}
