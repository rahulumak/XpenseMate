package com.umacai.xpensemate.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.umacai.xpensemate.data.local.dao.CategoryDao
import com.umacai.xpensemate.data.local.dao.PaymentMethodDao
import com.umacai.xpensemate.data.local.dao.TransactionDao
import com.umacai.xpensemate.data.local.entity.CategoryEntity
import com.umacai.xpensemate.data.local.entity.PaymentMethodEntity
import com.umacai.xpensemate.data.local.entity.TransactionEntity
import com.umacai.xpensemate.utils.DateConverter

@Database(
    entities = [TransactionEntity::class, CategoryEntity::class, PaymentMethodEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class XpenseMateDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun paymentMethodDao(): PaymentMethodDao
}