package com.umacai.xpensemate.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.umacai.xpensemate.data.local.database.XpenseMateDatabase
import com.umacai.xpensemate.data.local.entity.CategoryEntity
import com.umacai.xpensemate.data.local.entity.PaymentMethodEntity
import com.umacai.xpensemate.data.local.entity.TransactionEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): XpenseMateDatabase {
        return Room.databaseBuilder(
            context,
            XpenseMateDatabase::class.java,
            "xpensemate_database"
        ).fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        val database = provideDatabase(context)
                        prefillData(database)
                    }
                }
            }).build()
    }

    @Provides
    fun provideTransactionDao(database: XpenseMateDatabase) = database.transactionDao()

    @Provides
    fun provideCategoryDao(database: XpenseMateDatabase) = database.categoryDao()

    @Provides
    fun providePaymentMethodDao(database: XpenseMateDatabase) = database.paymentMethodDao()

    private suspend fun prefillData(database: XpenseMateDatabase) {
        val categoryDao = database.categoryDao()
        val paymentMethodDao = database.paymentMethodDao()
        val transactionDao = database.transactionDao()

        // Pre-fill Categories
        val categories = listOf(
            CategoryEntity(name = "Food"),
            CategoryEntity(name = "Travel"),
            CategoryEntity(name = "Shopping"),
            CategoryEntity(name = "Entertainment"),
            CategoryEntity(name = "Bills")
        )
        categories.forEach { categoryDao.insertCategory(it) }

        // Pre-fill Payment Methods
        val paymentMethods = listOf(
            PaymentMethodEntity(name = "Credit Card"),
            PaymentMethodEntity(name = "Debit Card"),
            PaymentMethodEntity(name = "UPI"),
            PaymentMethodEntity(name = "Cash")
        )
        paymentMethods.forEach { paymentMethodDao.insertPaymentMethod(it) }

        // Fetch IDs
        val categoryList = categoryDao.getAllCategories()
        val paymentMethodList = paymentMethodDao.getAllPaymentMethods()

        // Pre-fill Transactions
        val transactions = listOf(
            TransactionEntity(
                description = "Lunch at a Restaurant",
                amount = 500.0,
                date = java.util.Date(),
                categoryId = categoryList.first { it.name == "Food" }.id,
                paymentMethodId = paymentMethodList.first { it.name == "Credit Card" }.id
            ),
            TransactionEntity(
                description = "Flight Ticket to Delhi",
                amount = 1200.0,
                date = java.util.Date(),
                categoryId = categoryList.first { it.name == "Travel" }.id,
                paymentMethodId = paymentMethodList.first { it.name == "UPI" }.id
            ),
            TransactionEntity(
                description = "Grocery Shopping",
                amount = 350.0,
                date = java.util.Date(),
                categoryId = categoryList.first { it.name == "Shopping" }.id,
                paymentMethodId = paymentMethodList.first { it.name == "Cash" }.id
            ),
            TransactionEntity(
                description = "Movie Night",
                amount = 600.0,
                date = java.util.Date(),
                categoryId = categoryList.first { it.name == "Entertainment" }.id,
                paymentMethodId = paymentMethodList.first { it.name == "Debit Card" }.id
            ),
            TransactionEntity(
                description = "Electricity Bill Payment",
                amount = 900.0,
                date = Date(),
                categoryId = categoryList.first { it.name == "Bills" }.id,
                paymentMethodId = paymentMethodList.first { it.name == "Credit Card" }.id
            )
        )
        transactions.forEach { transactionDao.insertTransaction(it) }
    }
}