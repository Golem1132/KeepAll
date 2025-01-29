package com.example.keepall.database

import androidx.room.TypeConverter
import com.example.keepall.model.NoteTextStyle
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Date

class Converters {
    @TypeConverter
    fun fromDateToLong(date: Date): Long = date.time

    @TypeConverter
    fun fromLongToDate(time: Long) = Date(time)

    @TypeConverter
    fun fromStyleListToJson(styleList: List<NoteTextStyle>) = Json.encodeToString<List<NoteTextStyle>>(styleList)

    @TypeConverter
    fun fromJsonToStyleList(json: String) = Json.decodeFromString<List<NoteTextStyle>>( json)
}