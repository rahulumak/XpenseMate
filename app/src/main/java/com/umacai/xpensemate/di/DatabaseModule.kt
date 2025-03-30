package com.umacai.xpensemate.di

import android.content.Context
import androidx.room.Room
import com.umacai.xpensemate.data.local.database.XpenseMateDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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