package com.umacai.xpensemate.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.SmsMessage
import com.umacai.xpensemate.data.local.entity.TransactionEntity
import com.umacai.xpensemate.domain.repository.TransactionRepository
import com.umacai.xpensemate.presentation.viewmodel.TransactionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {

    @Inject
    lateinit var transactionRepository: TransactionRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "android.provider.Telephony.SMS_RECEIVED") {
            val bundle: Bundle? = intent.extras
            if (bundle != null) {
                val pdus = bundle["pdus"] as Array<*>
                for (pdu in pdus) {
                    val message = SmsMessage.createFromPdu(pdu as ByteArray)
                    val msgBody = message.messageBody
                    val sender = message.originatingAddress ?: "Unknown"

                    if (msgBody.contains("debited") || msgBody.contains("credited")) {
                        saveTransaction(msgBody, sender)
                    }
                }
            }
        }
    }
    private fun saveTransaction(message: String, sender: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val amount = extractAmount(message)
            val transaction = TransactionEntity(
                description = "SMS from $sender",
                amount = amount,
                date = Date(),
                categoryId = 1,
                paymentMethodId = 1
            )
            transactionRepository.insertTransaction(transaction)
        }
    }

    private fun extractAmount(message: String): Double {
        val regex = Regex("[0-9]+\\.?[0-9]*")
        val match = regex.find(message)
        return match?.value?.toDoubleOrNull() ?: 0.0
    }
}