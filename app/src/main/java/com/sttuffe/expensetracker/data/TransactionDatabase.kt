package com.sttuffe.expensetracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase

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
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            fillInitialData(db)
                        }
                    })
                    .fallbackToDestructiveMigration() //TODO
                    .build()

                INSTANCE = instance
                instance
            }
        }

        // 첫 구동 시 예시용 초기 데이터 추가 함수
        private fun fillInitialData(db: SupportSQLiteDatabase) {
            val now = System.currentTimeMillis()

            db.execSQL(
                "INSERT INTO transaction_table (type, amount, content, date) " +
                        "VALUES ('EXPENSE', 1000, '예시1 (지출)', $now)"
            )

            db.execSQL(
                "INSERT INTO transaction_table (type, amount, content, date) " +
                        "VALUES ('INCOME', 1000, '예시2 (수입)', $now)"
            )
        }
    }
}