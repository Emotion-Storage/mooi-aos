package com.emotionstorage.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.emotionstorage.data.model.TimeCapsuleLocal
import com.emotionstorage.local.model.FavoriteTimeCapsuleRemoteKeyLocal
import com.emotionstorage.local.model.SessionLocal
import com.emotionstorage.local.model.TimeCapsuleRemoteKeyLocal
import com.emotionstorage.local.model.UserLocal
import com.emotionstorage.local.room.DtoConverter
import com.emotionstorage.local.room.dao.FavoriteTimeCapsuleRemoteKeyDao
import com.emotionstorage.local.room.dao.SessionDao
import com.emotionstorage.local.room.dao.TimeCapsuleDao
import com.emotionstorage.local.room.dao.TimeCapsuleRemoteKeyDao
import com.emotionstorage.local.room.dao.UserDao

object AppDatabaseConstant {
    const val NAME = "mooi-room-database"
    const val VERSION = 5

    object TableName {
        const val USER_TABLE = "user"
        const val SESSION_TABLE = "session"
        const val TIME_CAPSULE_TABLE = "time_capsule"
        const val TIME_CAPSULE_REMOTE_KEY_TABLE = "time_capsule_remote_key"
        const val FAVORITE_TIME_CAPSULE_REMOTE_KEY_TABLE = "favorite_time_capsule_remote_key"
    }
}

@Database(
    entities = [
        UserLocal::class,
        SessionLocal::class,
        TimeCapsuleLocal::class,
        TimeCapsuleRemoteKeyLocal::class,
        FavoriteTimeCapsuleRemoteKeyLocal::class
    ],
    version = AppDatabaseConstant.VERSION,
)
@TypeConverters(
    DtoConverter::class,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun sessionDao(): SessionDao

    abstract fun timeCapsuleDao(): TimeCapsuleDao

    abstract fun timeCapsuleRemoteKeyDao(): TimeCapsuleRemoteKeyDao

    abstract fun favoriteTimeCapsuleRemoteKeyDao(): FavoriteTimeCapsuleRemoteKeyDao
}
