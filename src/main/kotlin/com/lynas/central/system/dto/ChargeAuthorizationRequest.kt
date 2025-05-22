package com.lynas.central.system.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ChargeAuthorizationRequest(
    @JsonProperty("station_id")
    val stationId: String,
    @JsonProperty("driver_token")
    val driverToken: String,
    val status: String
)