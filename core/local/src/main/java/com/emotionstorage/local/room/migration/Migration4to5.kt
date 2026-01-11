package com.emotionstorage.local.room.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


internal val Migration4to5 =
    object : Migration(4, 5) {
        // add remote key tables
        override fun migrate(database: SupportSQLiteDatabase) {
            // new TimeCapsuleRemoteKey table & query index
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS time_capsule_remote_key (
                id INTEGER NOT NULL,
                queryKey TEXT NOT NULL,
                prevPage INTEGER,
                nextPage INTEGER,
                lastUpdated INTEGER NOT NULL,
                PRIMARY KEY(id)
            )
            """.trimIndent()
            )

            database.execSQL(
                """
            CREATE INDEX IF NOT EXISTS index_time_capsule_remote_key_queryKey
            ON time_capsule_remote_key(queryKey)
            """.trimIndent()
            )

            // new FavoriteTimeCapsuleRemoteKey table & query index
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS favorite_time_capsule_remote_key (
                id INTEGER NOT NULL,
                queryKey TEXT NOT NULL,
                prevPage INTEGER,
                nextPage INTEGER,
                lastUpdated INTEGER NOT NULL,
                PRIMARY KEY(id)
            )
            """.trimIndent()
            )

            database.execSQL(
                """
            CREATE INDEX IF NOT EXISTS index_favorite_time_capsule_remote_keys_queryKe
            ON favorite_time_capsule_remote_key(queryKey)
            """.trimIndent()
            )
        }
    }
