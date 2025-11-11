package com.sttuffe.expensetracker

import java.time.LocalDateTime


data class TransactionLog(
    val id: Long = 0,
    val type: TransactionType,
    val amount: Long,
    val content: String,
    val date: LocalDateTime
)