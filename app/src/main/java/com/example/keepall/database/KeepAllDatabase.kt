package com.example.keepall.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.keepall.data.Note

@Database(
    entities = [
        Note::class
               ],
<<<<<<< HEAD
    version = 8,
    exportSchema = true
=======
    version = 6,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 4, to = 5)
    ]
>>>>>>> c0771fe0f6a345d4d0ee783de90c4c3187897b93

)
@TypeConverters(Converters::class)
abstract class KeepAllDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}