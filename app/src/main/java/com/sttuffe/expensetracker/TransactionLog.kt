package com.sttuffe.expensetracker

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "transaction_table")
data class TransactionLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val type: TransactionType,
    val amount: Long,
    val content: String,
    val date: LocalDateTime
)