package com.lynas.central.system.mq.producer

import com.lynas.central.system.config.EXCHANGE_CHARGE_TRANSACTION
import com.lynas.central.system.config.ROUTE_KEY_CHARGE_TRANSACTION_START
import com.lynas.central.system.dto.ChargeTransactionStartRequest
import com.lynas.central.system.dto.CloudEvent
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ChargeTransactionMessageProducer(
    val template: RabbitTemplate
) {
    fun sendStartTransactionMessage(request: ChargeTransactionStartRequest) {
        val message = CloudEvent(
            subject = EXCHANGE_CHARGE_TRANSACTION,
            data = request,
            id = UUID.randomUUID().toString(),
            datacontenttype = MediaType.APPLICATION_JSON.toString()
        )
        template.convertAndSend(EXCHANGE_CHARGE_TRANSACTION, ROUTE_KEY_CHARGE_TRANSACTION_START, message)
    }
}