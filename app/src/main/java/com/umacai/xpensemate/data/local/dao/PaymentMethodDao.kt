package com.umacai.xpensemate.data.local.dao

import androidx.room.*
import com.umacai.xpensemate.data.local.entity.PaymentMethodEntity

@Dao
interface PaymentMethodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentMethod(paymentMethod: PaymentMethodEntity): Long

    @Query("SELECT * FROM payment_methods")
    suspend fun getAllPaymentMethods(): List<PaymentMethodEntity>

    @Query("DELETE FROM payment_methods WHERE id = :paymentMethodId")
    suspend fun deletePaymentMethod(paymentMethodId: Int)
}