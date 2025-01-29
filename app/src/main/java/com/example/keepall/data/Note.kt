package com.example.keepall.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.keepall.model.NoteTextStyle
import java.util.Date

@Entity(tableName = "notes_tbl")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(defaultValue = "No title")
    val title: String,
    @ColumnInfo(defaultValue = "Nothing here")
    val textContent: String,
    @ColumnInfo(typeAffinity = ColumnInfo.INTEGER)
    val color: Int,
    val style: List<NoteTextStyle>,
    val attachments: String,
    val dateAdded: Date?
)
