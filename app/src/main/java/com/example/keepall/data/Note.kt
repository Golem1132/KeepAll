package com.example.keepall.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes_tbl")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(defaultValue = "No title")
    val title: String,
    @ColumnInfo(defaultValue = "Nothing here")
    val textContent: String,
    val attachments: String,
    val dateAdded: Date?
)
