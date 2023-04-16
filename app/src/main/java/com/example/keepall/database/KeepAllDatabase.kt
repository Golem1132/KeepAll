package com.example.keepall.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.keepall.data.Note

@Database(
    entities = [
        Note::class
               ],
    version = 1,
    exportSchema = true,

)
abstract class KeepAllDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}