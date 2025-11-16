package com.sttuffe.expensetracker.data

import androidx.room.TypeConverter
import com.sttuffe.expensetracker.TransactionType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class Converter {
    // 날짜 변환: LocalDateTime <-> Long
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            Instant.ofEpochMilli(it)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneId.systemDefault())
            ?.toInstant()
            ?.toEpochMilli()
    }

    // 유형 변환: TransactionType <-> String
    @TypeConverter
    fun fromType(value: String): TransactionType {
        return TransactionType.valueOf(value)
    }

    @TypeConverter
    fun typeToString(type: TransactionType): String {
        return type.name
    }
}