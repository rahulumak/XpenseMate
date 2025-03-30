package com.umacai.xpensemate.di

import com.umacai.xpensemate.data.local.dao.CategoryDao
import com.umacai.xpensemate.data.local.dao.PaymentMethodDao
import com.umacai.xpensemate.data.local.dao.TransactionDao
import com.umacai.xpensemate.data.repository.CategoryRepositoryImpl
import com.umacai.xpensemate.data.repository.PaymentMethodRepositoryImpl
import com.umacai.xpensemate.data.repository.TransactionRepositoryImpl
import com.umacai.xpensemate.domain.repository.CategoryRepository
import com.umacai.xpensemate.domain.repository.PaymentMethodRepository
import com.umacai.xpensemate.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTransactionRepository(dao: TransactionDao): TransactionRepository {
        return TransactionRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(dao: CategoryDao): CategoryRepository {
        return CategoryRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun providePaymentMethodRepository(dao: PaymentMethodDao): PaymentMethodRepository {
        return PaymentMethodRepositoryImpl(dao)
    }
}