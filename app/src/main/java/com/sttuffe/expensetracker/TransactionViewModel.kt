package com.sttuffe.expensetracker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sttuffe.expensetracker.data.TransactionDAO
import com.sttuffe.expensetracker.data.TransactionLog
import com.sttuffe.expensetracker.data.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class TransactionViewModel(
    private val dao: TransactionDAO
) : ViewModel() {
    private val _transactions = MutableStateFlow<List<TransactionLog>>(emptyList())

    val transactions: StateFlow<List<TransactionLog>> = _transactions.asStateFlow()

    init {
        viewModelScope.launch {
            dao.getAllTransactions().collect { list ->
                _transactions.value = list
            }
        }
    }

    fun addTransaction(
        type: TransactionType,
        amountString: String,
        content: String,
        dateMillis: Long?
    ) {
        // 금액 파싱
        val amount = amountString.toLongOrNull() ?: 0L

        // 날짜 파싱
        val date = if (dateMillis != null) {
            Instant.ofEpochMilli(dateMillis)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
        } else {
            LocalDateTime.now()
        }

        // 새 TransactionLog 생성
        val newLog = TransactionLog(
            // id=0인 경우 Room에서 자동 처리
            id = 0,
            type = type,
            amount = amount,
            content = content,
            date = date
        )

        viewModelScope.launch {
            dao.insertTransaction(newLog)
        }
    }
}

class TransactionViewModelFactory(private val dao: TransactionDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}