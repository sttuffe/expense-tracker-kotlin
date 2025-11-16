package com.sttuffe.expensetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sttuffe.expensetracker.TransactionLog

@Database(
    entities = [TransactionLog::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class TransactionDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDAO

    companion object {
        @Volatile
        private var INSTANCE: TransactionDatabase? = null

        fun getDatabase(context: Context): TransactionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDatabase::class.java,
                    "transaction_database"
                )
                    .fallbackToDestructiveMigration() //TODO
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}