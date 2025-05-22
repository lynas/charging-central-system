package com.lynas.central.system.dto

data class ErrorResponse(
    val errors: Map<String, String>,
    val code: String,
    val timestamp: String
)

data class SuccessResponse(
    val message: String,
    val status: String,
)