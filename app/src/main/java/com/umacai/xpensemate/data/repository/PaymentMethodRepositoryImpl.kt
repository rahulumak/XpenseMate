package com.umacai.xpensemate.data.repository

import com.umacai.xpensemate.data.local.dao.PaymentMethodDao
import com.umacai.xpensemate.data.local.entity.PaymentMethodEntity
import com.umacai.xpensemate.domain.repository.PaymentMethodRepository
import javax.inject.Inject

class PaymentMethodRepositoryImpl @Inject constructor(
    private val paymentMethodDao: PaymentMethodDao
) : PaymentMethodRepository {

    override suspend fun insertPaymentMethod(paymentMethod: PaymentMethodEntity): Long {
        return paymentMethodDao.insertPaymentMethod(paymentMethod)
    }

    override suspend fun getAllPaymentMethods(): List<PaymentMethodEntity> {
        return paymentMethodDao.getAllPaymentMethods()
    }

    override suspend fun deletePaymentMethod(paymentMethodId: Int) {
        paymentMethodDao.deletePaymentMethod(paymentMethodId)
    }
}