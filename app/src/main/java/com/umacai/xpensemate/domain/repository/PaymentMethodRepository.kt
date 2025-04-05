package com.umacai.xpensemate.domain.repository

import com.umacai.xpensemate.data.local.entity.PaymentMethodEntity

interface PaymentMethodRepository {

    suspend fun insertPaymentMethod(paymentMethod: PaymentMethodEntity): Long

    suspend fun getAllPaymentMethods(): List<PaymentMethodEntity>

    suspend fun deletePaymentMethod(paymentMethodId: Int)

    suspend fun getPaymentMethodIdFromSMSText(smsText: String): Int?
}