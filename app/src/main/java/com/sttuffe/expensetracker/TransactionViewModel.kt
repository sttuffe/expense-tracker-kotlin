package com.sttuffe.expensetracker

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDateTime

class TransactionViewModel: ViewModel() {
    private val _transactions = MutableStateFlow<List<TransactionLog>>(emptyList())

    val transactions: StateFlow<List<TransactionLog>> = _transactions.asStateFlow()

    init {
        //TODO:TestData
        loadTestData()
    }

    private fun loadTestData() {
        _transactions.value = listOf(
            TransactionLog(1, TransactionType.EXPENSE, 1000, "test1", LocalDateTime.now()),
            TransactionLog(2, TransactionType.INCOME, 999999000, "test2", LocalDateTime.now().minusDays(5)),
            TransactionLog(3, TransactionType.EXPENSE, 3000, "test3", LocalDateTime.now().minusDays(1)),
            TransactionLog(4, TransactionType.EXPENSE, 4000, "test4", LocalDateTime.now().minusHours(2))
        ).sortedByDescending { it.date }
    }
}