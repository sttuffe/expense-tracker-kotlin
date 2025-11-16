package com.sttuffe.expensetracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sttuffe.expensetracker.TransactionLog
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDAO {
    // 내역 조회
    @Query("SELECT * FROM transaction_table ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionLog>>

    // 내역 추가
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionLog)
}