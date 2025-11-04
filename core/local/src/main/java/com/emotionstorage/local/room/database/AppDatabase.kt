package com.emotionstorage.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.emotionstorage.local.model.SessionLocal
import com.emotionstorage.local.model.TimeCapsuleLocal
import com.emotionstorage.local.model.UserLocal
import com.emotionstorage.local.room.DtoConverter
import com.emotionstorage.local.room.dao.SessionDao
import com.emotionstorage.local.room.dao.TimeCapsuleDao
import com.emotionstorage.local.room.dao.UserDao

object AppDatabaseConstant {
    const val NAME = "mooi-room-database"
    const val VERSION = 3

    object TableName {
        const val USER_TABLE = "user"
        const val SESSION_TABLE = "session"
        const val TIME_CAPSULE_TABLE = "time_capsule"
    }
}

@Database(
    entities = [UserLocal::class, SessionLocal::class, TimeCapsuleLocal::class],
    version = AppDatabaseConstant.VERSION,
)
@TypeConverters(
    DtoConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun sessionDao(): SessionDao

    abstract fun timeCapsuleDao(): TimeCapsuleDao
}
