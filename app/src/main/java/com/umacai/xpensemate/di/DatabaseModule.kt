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
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideTransactionDao(database: XpenseMateDatabase) = database.transactionDao()

    @Provides
    fun provideCategoryDao(database: XpenseMateDatabase) = database.categoryDao()

    @Provides
    fun providePaymentMethodDao(database: XpenseMateDatabase) = database.paymentMethodDao()


}