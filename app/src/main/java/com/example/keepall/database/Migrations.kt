package com.example.keepall.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE notes_tbl")
        database.execSQL("CREATE TABLE notes_tbl (id int NOT NULL PRIMARY KEY, textContent text NOT NULL, canvas text, photos text)")
    }
}

val migration_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE notes_tbl")
        database.execSQL("CREATE TABLE notes_tbl (id int NOT NULL PRIMARY KEY, textContent text, canvas text, photos text)")
    }
}

val migration_3_4 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE notes_tbl")
        database.execSQL("CREATE TABLE notes_tbl (id int NOT NULL PRIMARY KEY, textContent text NOT NULL DEFAULT '', canvas text DEFAULT '', photos text DEFAULT '')")
    }
}

val migration_5_6 = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE notes_tbl")
        database.execSQL("CREATE TABLE notes_tbl (id int NOT NULL PRIMARY KEY, textContent text NOT NULL DEFAULT 'Nothing', canvas text DEFAULT '', photos text DEFAULT '', dateAdded int DEFAULT 'NULL')")
    }
}