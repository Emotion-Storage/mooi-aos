package com.emotionstorage.local.room.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal val Migration3to4 =
    object : Migration(3, 4) {
        // delete id column from user table
        override fun migrate(database: SupportSQLiteDatabase) {
            // new user table without id
            database.execSQL(
                """
                CREATE TABLE IF NOT EXISTS `user_new` (
                    `pk` INTEGER PRIMARY KEY NOT NULL,
                    `socialType` TEXT NOT NULL,
                    `socialId` TEXT NOT NULL,
                    `email` TEXT NOT NULL,
                    `name` TEXT NOT NULL,
                    `profileImageUrl` TEXT,
                    `createdAt` TEXT NOT NULL,
                    `updatedAt` TEXT NOT NULL
                )
                """.trimIndent(),
            )

            // copy from user to user_new
            database.execSQL(
                """
                INSERT INTO `user_new`
                    (`pk`, `socialType`, `socialId`, `email`, `name`, `profileImageUrl`, `createdAt`, `updatedAt`)
                SELECT
                    `pk`, `socialType`, `socialId`, `email`, `name`, `profileImageUrl`, `createdAt`, `updatedAt`
                FROM `user`
                """.trimIndent(),
            )

            database.execSQL("DROP TABLE `user`")
            database.execSQL("ALTER TABLE `user_new` RENAME TO `user`")
        }
    }
