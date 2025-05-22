package com.lynas.central.system.service

import com.lynas.central.system.dto.ChargeTransactionStartRequest
import com.lynas.central.system.mq.producer.ChargeTransactionMessageProducer
import org.springframework.stereotype.Service

@Service
class ChargeTransactionService(val producer: ChargeTransactionMessageProducer) {
    fun startCharging(request: ChargeTransactionStartRequest) {
        producer.sendStartTransactionMessage(request)
    }
}