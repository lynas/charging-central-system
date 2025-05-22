package com.lynas.central.system.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lynas.central.system.BaseTest
import com.lynas.central.system.dto.ChargeTransactionStartRequest
import com.lynas.central.system.service.ChargeTransactionService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import java.net.URI
import java.util.*

class ChargeTransactionControllerTest: BaseTest() {

    @Autowired
    lateinit var chargeTransactionService: ChargeTransactionService

    private val url = "/charge-transactions"

    @TestConfiguration
    class ControllerTestConfig {
        @Bean
        fun chargeTransactionService(): ChargeTransactionService = mock()
    }

    @Test
    fun `should accept valid charge request`() {
        val request = ChargeTransactionStartRequest(
            stationId = UUID.randomUUID(),
            driverId = "ABCDEFGHIJKLMNOPQRST",
            callBackUrl = URI("http://localhost/callback").toURL()
        )

        mockMvc.post(url) {
            content = jacksonObjectMapper().writeValueAsString(request)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect { status { isAccepted() } }
    }

    @Test
    fun `should get bad request due to bad driver id`() {
        val request = ChargeTransactionStartRequest(
            stationId = UUID.randomUUID(),
            driverId = "ABCDEF",
            callBackUrl = URI("http://localhost/callback").toURL()
        )

        mockMvc.post(url) {
            content = jacksonObjectMapper().writeValueAsString(request)
            contentType = MediaType.APPLICATION_JSON
        }.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `should get bad request due to bad callBackUrl`() {
        val request = """
            {
              "stationId": "2b17a6ab-cbca-40c0-a2dd-e4123dc30d03",
              "driverId": "2b17a6ab-cbca-40c0-a2dd-e4123dc30d01",
              "callBackUrl": "invalid"
            }
        """.trimIndent()

        mockMvc.post(url) {
            content = request
            contentType = MediaType.APPLICATION_JSON
        }.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `should get bad request due to bad stationId`() {
        val request = """
            {
              "stationId": "2b17a6ab",
              "driverId": "2b17a6ab-cbca-40c0-a2dd-e4123dc30d01",
              "callBackUrl": "http://localhost/callback"
            }
        """.trimIndent()

        mockMvc.post(url) {
            content = request
            contentType = MediaType.APPLICATION_JSON
        }.andExpect { status { isBadRequest() } }
    }

}