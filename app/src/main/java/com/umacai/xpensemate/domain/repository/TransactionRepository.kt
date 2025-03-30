package com.umacai.xpensemate.domain.repository

import com.umacai.xpensemate.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun insertTransaction(transaction: TransactionEntity)

    fun getAllTransactions(): Flow<List<TransactionEntity>>

    suspend fun deleteTransaction(transactionId: Int)
}