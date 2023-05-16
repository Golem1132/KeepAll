package com.example.keepall.database

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromDateToLong(date: Date): Long = date.time

    @TypeConverter
    fun fromLongToDate(time: Long) = Date(time)
}