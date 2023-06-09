package com.example.keepall.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.keepall.data.Note

@Database(
    entities = [
        Note::class
               ],
    version = 6,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 4, to = 5)
    ]

)
@TypeConverters(Converters::class)
abstract class KeepAllDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}