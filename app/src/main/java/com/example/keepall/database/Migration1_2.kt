package com.example.keepall.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration1_2(startVersion: Int, endVersion: Int) : Migration(startVersion, endVersion) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE notes_tbl")
        database.execSQL("CREATE TABLE notes_tbl (id int NOT NULL PRIMARY KEY, textContent text NOT NULL, canvas text, photos text)")
    }
}