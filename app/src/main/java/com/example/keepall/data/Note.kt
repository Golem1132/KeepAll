package com.example.keepall.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_tbl")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val textContent: String,
    val canvas: String?,
    val photos: String?
)
