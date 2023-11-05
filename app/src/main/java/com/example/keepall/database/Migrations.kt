package com.example.keepall.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_6_TO_7 = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE notes_tbl")
        database.execSQL("CREATE TABLE notes_tbl (id INTEGER NOT NULL PRIMARY KEY, title TEXT NOT NULL DEFAULT 'No title', textContent TEXT NOT NULL DEFAULT 'Nothing here', attachments TEXT NOT NULL, dateAdded INTEGER DEFAULT 'NULL')")
    }

}