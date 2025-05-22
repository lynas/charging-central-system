package com.lynas.central.system.mq.consumer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lynas.central.system.config.QUEUE_CHARGE_TRANSACTION_START
import com.lynas.central.system.dto.ChargeTransactionStartRequest
import com.lynas.central.system.dto.CloudEvent
import com.lynas.central.system.service.ChargeTransactionAuthorizationService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class ChargeTransactionMessageConsumer(
    val chargeTransactionAuthorizationService: ChargeTransactionAuthorizationService
) {
    @RabbitListener(queues = [QUEUE_CHARGE_TRANSACTION_START], containerFactory ="listenerContainerFactory" )
    fun consume(cloudEvent: CloudEvent) {
        val startRequest: ChargeTransactionStartRequest = jacksonObjectMapper()
            .convertValue(cloudEvent.data, ChargeTransactionStartRequest::class.java)
        chargeTransactionAuthorizationService.createNewChargeTransactionAuthorization(startRequest)
    }
}