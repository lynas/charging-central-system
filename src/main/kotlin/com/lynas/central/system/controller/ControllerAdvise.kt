package com.lynas.central.system.controller

import com.lynas.central.system.dto.ErrorResponse
import com.lynas.central.system.getErrorMessageForInvalidInput
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant


@RestControllerAdvice
class ControllerAdvise {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentTypeMismatchException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            getErrorMessageForInvalidInput(ex),
            "INVALID_INPUT",
            Instant.now().toString()
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMethodArgumentTypeMismatchException(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            getErrorMessageForInvalidInput(ex),
            "INVALID_INPUT",
            Instant.now().toString()
        )
        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }

}
