package com.umacai.xpensemate.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.umacai.xpensemate.data.local.entity.CategoryEntity
import com.umacai.xpensemate.data.local.entity.PaymentMethodEntity
import com.umacai.xpensemate.data.local.entity.TransactionEntity
import com.umacai.xpensemate.domain.repository.CategoryRepository
import com.umacai.xpensemate.domain.repository.PaymentMethodRepository
import com.umacai.xpensemate.domain.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val paymentMethodRepository: PaymentMethodRepository
) : ViewModel() {

    private val _transactions = MutableStateFlow<List<TransactionEntity>>(emptyList())
    val transactions: StateFlow<List<TransactionEntity>> = _transactions

    private val _categories = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categories: StateFlow<List<CategoryEntity>> = _categories

    private val _paymentMethods = MutableStateFlow<List<PaymentMethodEntity>>(emptyList())
    val paymentMethods: StateFlow<List<PaymentMethodEntity>> = _paymentMethods

    init {
        fetchTransactions()
        fetchCategories()
        fetchPaymentMethods()
    }

    fun fetchTransactions() {
        viewModelScope.launch {
            transactionRepository.getAllTransactions().collect { transactionsList ->
                _transactions.value = transactionsList
            }
        }
    }

    fun fetchCategories() {
        viewModelScope.launch {
            _categories.value = categoryRepository.getAllCategories()
        }
    }

    fun fetchPaymentMethods() {
        viewModelScope.launch {
            _paymentMethods.value = paymentMethodRepository.getAllPaymentMethods()
        }
    }

    fun parseAndSaveTransaction(message: String, sender: String) {
        viewModelScope.launch {
            val amount = extractAmount(message)
            val categoryId = categories.value.firstOrNull()?.id ?: 1
            val paymentMethodId = paymentMethods.value.firstOrNull()?.id ?: 1

            val transaction = TransactionEntity(
                description = "SMS from $sender",
                amount = amount,
                date = Date(),
                categoryId = categoryId,
                paymentMethodId = paymentMethodId
            )
            transactionRepository.insertTransaction(transaction)
            fetchTransactions()
        }
    }

    private fun extractAmount(message: String): Double {
        val regex = Regex("[0-9]+\\.?[0-9]*")
        val match = regex.find(message)
        return match?.value?.toDoubleOrNull() ?: 0.0
    }
}