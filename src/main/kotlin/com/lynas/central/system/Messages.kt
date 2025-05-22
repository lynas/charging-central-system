package com.lynas.central.system

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import java.net.MalformedURLException
import java.util.UUID
import kotlin.collections.associate
import kotlin.collections.firstOrNull
import kotlin.jvm.java

fun getErrorMessageForInvalidInput(ex: MethodArgumentNotValidException): Map<String, String> {
    val errors: Map<String, String> =
        ex.bindingResult.fieldErrors.associate { fieldError ->
            val field = fieldError.field
            val msg = fieldError.defaultMessage ?: "Invalid Input"
            field to msg
        }
    return errors
}


fun getErrorMessageForInvalidInput(ex: HttpMessageNotReadableException): Map<String, String> {
    val rootCause = ex.rootCause
    val errorMessage: Map<String, String> = when (rootCause) {
        is InvalidFormatException -> {
            val targetType = rootCause.targetType
            val fieldName = rootCause.path.firstOrNull()?.fieldName ?: "field"
            if (targetType == UUID::class.java && fieldName == "stationId") {
                mapOf("stationId" to "stationId must be a valid UUID")
            } else {
                mapOf(fieldName to "Invalid value for field")
            }
        }
        is MalformedURLException -> {
            mapOf("error" to "Invalid URL sent in request body")
        }
        else -> mapOf("error" to "Malformed JSON request")
    }
    return errorMessage
}
