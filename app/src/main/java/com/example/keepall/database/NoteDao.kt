package com.example.keepall.database

import androidx.room.*
import com.example.keepall.data.Note

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes_tbl")
    fun getAllNotes(): List<Note>

    @Query("SELECT * FROM notes_tbl WHERE id =:id")
    fun getNote(id: Int): Note

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertNote(note: Note)

    @Delete
    fun deleteNote(note: Note)
}