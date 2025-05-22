package com.lynas.central.system.dto

import jakarta.validation.constraints.Pattern
import java.net.URL
import java.util.UUID

data class ChargeTransactionStartRequest(
    val stationId: UUID,
    @field:Pattern(regexp = "^[a-zA-Z0-9\\-\\._~]{20,80}$", message = "{driverId.pattern}")
    val driverId: String,
    val callBackUrl: URL,
)