package com.umacai.xpensemate.data.repository
import com.umacai.xpensemate.data.local.dao.TransactionDao
import com.umacai.xpensemate.data.local.entity.TransactionEntity
import com.umacai.xpensemate.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {

    override suspend fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
    }

    override fun getAllTransactions(): Flow<List<TransactionEntity>> {
        return transactionDao.getAllTransactions()
    }

    override suspend fun deleteTransaction(transactionId: Int) {
        transactionDao.deleteTransaction(transactionId)
    }
}