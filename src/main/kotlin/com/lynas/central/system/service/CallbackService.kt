package com.lynas.central.system.service

import com.lynas.central.system.dto.ChargeAuthorizationRequest
import com.lynas.central.system.dto.ChargeTransactionStartRequest
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.net.URI
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutorService

@Service
class CallbackService(
    private val restClient: RestClient,
    private val executorService: ExecutorService
) {
    fun sendAuthorizeChargeTransactionCallbackAsync(startRequest: ChargeTransactionStartRequest, status: String){
        CompletableFuture.supplyAsync<Any>({
            sendAuthorizeChargeTransactionCallback(startRequest, status)
        }, executorService)
    }

    fun sendAuthorizeChargeTransactionCallback(startRequest: ChargeTransactionStartRequest, status: String){
        val uri = URI("${startRequest.callBackUrl}")
        val body = ChargeAuthorizationRequest(
            stationId = startRequest.stationId.toString(),
            driverToken = startRequest.driverId,
            status = status
        )
        restClient.post()
            .uri(uri)
            .body(body)
            .retrieve()
            .body<String>()
    }
}