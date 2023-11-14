package com.example.keepall.database

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromDateToLong(date: Date): Long = date.time

    @TypeConverter
    fun fromLongToDate(time: Long) = Date(time)
}