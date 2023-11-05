package com.example.keepall.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.keepall.data.Note

@Database(
    entities = [
        Note::class
               ],
    version = 7,
    exportSchema = true

)
@TypeConverters(Converters::class)

abstract class KeepAllDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}