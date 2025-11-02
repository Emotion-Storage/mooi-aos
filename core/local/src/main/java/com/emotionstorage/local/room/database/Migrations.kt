package com.emotionstorage.local.room.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal val MIGRATION_2_3 = object : Migration(2, 3) {
    // create time capsule table
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS `time_capsule` " +
                "(" +
                "`id` LONG PRIMARY KEY NOT NULL," +
                "`status` TEXT NOT NULL," +
                "`title` TEXT NOT NULL," +
                "`summary` TEXT NOT NULL," +
                "`isFavorite` INTEGER NOT NULL," +
                "`emotions` TEXT NOT NULL," +
                "`comments` TEXT NOT NULL," +
                "`note` TEXT NOT NULL," +
                "`historyDate` TEXT NOT NULL," +
                "`createdAt` TEXT NOT NULL," +
                "`updatedAt` TEXT NOT NULL," +
                "`openAt` TEXT," +
                "`favoriteAt` TEXT" +
                ");"
        )
    }
}
