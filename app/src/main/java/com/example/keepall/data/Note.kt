package com.example.keepall.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "notes_tbl")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(defaultValue = "Nothing")
    val textContent: String,
    val canvas: String?,
    val photos: String?,
    val dateAdded: Date?
)
