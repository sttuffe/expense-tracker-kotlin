package com.sttuffe.expensetracker

import android.app.Application
import com.sttuffe.expensetracker.data.TransactionDatabase

class ExpenseApplication : Application() {

    // by lazy: 앱의 첫 구동에만 동작
    val database by lazy { TransactionDatabase.getDatabase(this) }
}