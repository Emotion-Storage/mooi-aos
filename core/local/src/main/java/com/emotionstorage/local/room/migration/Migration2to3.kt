package com.emotionstorage.local.room.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal val Migration2to3 =
    object : Migration(2, 3) {
        // create time capsule table & add indices
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `time_capsule`(
                        `id` INTEGER PRIMARY KEY NOT NULL,
                        `status` TEXT NOT NULL,
                        `title` TEXT NOT NULL,
                        `summary` TEXT NOT NULL,
                        `isFavorite` INTEGER NOT NULL,
                        `emotions` TEXT NOT NULL,
                        `comments` TEXT NOT NULL,
                        `note` TEXT NOT NULL,
                        `historyDate` TEXT NOT NULL,
                        `createdAt` TEXT NOT NULL,
                        `updatedAt` TEXT NOT NULL,
                        `openAt` TEXT,
                        `favoriteAt` TEXT
                )
                """.trimIndent(),
            )

            database.execSQL(
                "CREATE INDEX IF NOT EXISTS `index_time_capsule_status` " +
                    "ON `time_capsule`(`status`);",
            )
            database.execSQL(
                "CREATE INDEX IF NOT EXISTS `index_time_capsule_isFavorite` " +
                    "ON `time_capsule`(`isFavorite`);",
            )
            database.execSQL(
                "CREATE INDEX IF NOT EXISTS `index_time_capsule_openAt` " +
                    "ON `time_capsule`(`openAt`);",
            )
        }
    }
