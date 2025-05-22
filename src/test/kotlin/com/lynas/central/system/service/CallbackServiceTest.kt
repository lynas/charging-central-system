package com.lynas.central.system.service

import com.lynas.central.system.dto.ChargeTransactionStartRequest
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.springframework.http.HttpMethod
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.content
import org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import java.util.concurrent.ExecutorService
import kotlin.test.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import java.net.URI
import java.util.UUID


class CallbackServiceTest {

    private lateinit var executorService: ExecutorService
    private lateinit var callbackService: CallbackService
    private lateinit var restTemplate: RestTemplate
    private lateinit var restClient: RestClient
    private lateinit var mockServer: MockRestServiceServer

    @BeforeEach
    fun setUp() {
        executorService = mock()

        restTemplate = RestTemplate()
        mockServer = MockRestServiceServer.createServer(restTemplate)
        restClient = RestClient.create(restTemplate)

        callbackService = CallbackService(restClient, executorService)
    }

    @Test
    fun `should send authorize charge transaction callback with correct payload`() {
        val request = ChargeTransactionStartRequest(
            driverId = UUID.randomUUID().toString(),
            stationId = UUID.randomUUID(),
            callBackUrl = URI("http://localhost/callback").toURL()
        )
        mockServer.expect(requestTo(URI.create("http://localhost/callback")))
            .andExpect(method(HttpMethod.POST))
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(content().json(
                """
                {
                    "station_id": "${request.stationId}",
                    "driver_token": "${request.driverId}",
                    "status": "ALLOWED"
                }
                """.trimIndent()
            ))
            .andRespond(withSuccess("OK", APPLICATION_JSON))

        callbackService.sendAuthorizeChargeTransactionCallback(request, "ALLOWED")

        mockServer.verify()
    }

}