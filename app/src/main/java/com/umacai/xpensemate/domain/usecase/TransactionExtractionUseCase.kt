package com.umacai.xpensemate.domain.usecase

import com.umacai.xpensemate.data.local.entity.TransactionEntity
import com.umacai.xpensemate.domain.repository.CategoryRepository
import com.umacai.xpensemate.domain.repository.PaymentMethodRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.regex.Pattern

class TransactionExtractionUseCase {
    fun extractTransactionDetails(
        smsBody: String,
        sender: String,
        categoryRepository: CategoryRepository,
        paymentMethodRepository: PaymentMethodRepository
    ): TransactionEntity? {
        // Convert SMS to lowercase for better matching
        val lowerBody = smsBody.lowercase()

        // Determine Credit or Debit using additional keywords
        val isCredit = when {
            lowerBody.contains("credited") || lowerBody.contains("deposit") || lowerBody.contains("deposited") -> true
            lowerBody.contains("debited") || lowerBody.contains("withdrawal") || lowerBody.contains(
                "spent"
            ) ||
                    lowerBody.contains("trn") || lowerBody.contains("transaction") -> false

            else -> return null // Invalid or unrelated SMS
        }

        // Extract Transaction Amount using Regex
        val amountPattern =
            Pattern.compile("(?:rs|inr|₹)\\.?\\s*([0-9]{1,3}(?:,[0-9]{3})*(?:\\.[0-9]{1,2})?|[0-9]+(?:\\.[0-9]{1,2})?)", Pattern.CASE_INSENSITIVE)
        val amountMatcher = amountPattern.matcher(smsBody)
        val amount =
            if (amountMatcher.find()) amountMatcher.group(1)?.toDoubleOrNull() ?: 0.0 else 0.0

        if (amount == 0.0) return null // Invalid amount

        // Extract Account or Card Number (e.g., XXXX1234 or x1234)
        val accountPattern = Pattern.compile("(?:xxxx|x)\\s*([0-9]{4})", Pattern.CASE_INSENSITIVE)
        val accountMatcher = accountPattern.matcher(smsBody)
        val accountOrCardNumber =
            if (accountMatcher.find()) "XXXX${accountMatcher.group(1)}" else null

        // Determine Payment Method
        val paymentMethod = when {
            lowerBody.contains("upi") -> "UPI"
            lowerBody.contains("credit card") -> "Credit Card"
            lowerBody.contains("debit card") -> "Debit Card"
            lowerBody.contains("cash") -> "Cash"
            lowerBody.contains("net banking") -> "Net Banking"
            else -> accountOrCardNumber ?: "Unknown"
        }

        // Extract Description (Fallback from Sender or extract from body)
        val description = when {
            lowerBody.contains("amazon") -> "Amazon Purchase"
            lowerBody.contains("flipkart") -> "Flipkart Purchase"
            lowerBody.contains("swiggy") -> "Swiggy Order"
            lowerBody.contains("zomato") -> "Zomato Order"
            lowerBody.contains("paytm") -> "Paytm Transaction"
            lowerBody.contains("atm") -> "ATM Withdrawal"
            else -> "Transaction with $sender"
        }

        // Determine Category Based on Description
        val categoryType = when {
            description.contains("swiggy") || description.contains("zomato") -> "Food"
            description.contains("amazon") || description.contains("flipkart") -> "Shopping"
            description.contains("atm") -> "Cash Withdrawal"
            description.contains("paytm") -> "Wallet Transfer"
            lowerBody.contains("bill") || lowerBody.contains("electricity") -> "Bills"
            lowerBody.contains("fuel") -> "Fuel"
            lowerBody.contains("rent") -> "Rent"
            else -> "Miscellaneous"
        }

        // Extract Where You Spent using "at" or "using"
        val spentAtPattern = Pattern.compile("(?:at|using)\\s+([a-zA-Z0-9\\s]+?)(?:\\.|\\s|$)")
        val spentAtMatcher = spentAtPattern.matcher(smsBody)
        val spentAt = if (spentAtMatcher.find()) spentAtMatcher.group(1)?.trim() else null
        var paymentMethodId:Int?=null
        var categoryId:Int?=null
        CoroutineScope(Dispatchers.IO).launch {
            paymentMethodId=paymentMethodRepository.getPaymentMethodIdFromSMSText(paymentMethod)
            categoryId=categoryRepository.getCategoryIdFromSMSText(categoryType)
        }

        return TransactionEntity(
            amount = amount,
            description = "$description at $spentAt",
            paymentMethodId = paymentMethodId?:1,
            isExpense = isCredit,
            categoryId = categoryId?:1,
            date = Date()
        )
    }

    fun isFinancialTransaction(sender: String,smsBody: String): Boolean {
        val keywords = listOf(
            "credited",
            "credit",
            "debited",
            "deducted",
            "withdrawal",
            "deposit",
            "deposited",
            "spent",
            "transaction",
            "trn",
            "payment",
            "purchase",
            "order",
            "transfer"
        )
        return keywords.any { smsBody.lowercase().contains(it) }
    }
}