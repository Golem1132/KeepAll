package com.example.keepall.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

<<<<<<< HEAD
val MIGRATION_6_TO_7 = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE notes_tbl")
        database.execSQL("CREATE TABLE notes_tbl (id INTEGER NOT NULL PRIMARY KEY, title TEXT NOT NULL DEFAULT 'No title', textContent TEXT NOT NULL DEFAULT 'Nothing here', attachments TEXT NOT NULL, dateAdded INTEGER DEFAULT 'NULL')")
    }

}

val MIGRATION_7_TO_8 = object : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE notes_tbl")
        database.execSQL("CREATE TABLE notes_tbl (id INTEGER NOT NULL PRIMARY KEY, title TEXT NOT NULL DEFAULT 'No title', textContent TEXT NOT NULL DEFAULT 'Nothing here', color INTEGER NOT NULL, attachments TEXT NOT NULL, dateAdded INTEGER DEFAULT 'NULL')")
=======
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
>>>>>>> c0771fe0f6a345d4d0ee783de90c4c3187897b93
    }
}