package com.lynas.central.system.controller

import com.lynas.central.system.dto.ChargeTransactionStartRequest
import com.lynas.central.system.dto.SuccessResponse
import com.lynas.central.system.service.ChargeTransactionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/charge-transactions")
class ChargeTransactionController(
    val chargeTransactionService: ChargeTransactionService
) {

    @PostMapping("")
    fun startCharging(@Valid @RequestBody chargeTransactionStartRequest: ChargeTransactionStartRequest): ResponseEntity<SuccessResponse> {
        chargeTransactionService.startCharging(chargeTransactionStartRequest)
        return ResponseEntity(
            SuccessResponse(
                message = "Request is being processed asynchronously. The result will be sent to the provided callback URL.",
                status = HttpStatus.ACCEPTED.name
            ), HttpStatus.ACCEPTED
        )
    }
}