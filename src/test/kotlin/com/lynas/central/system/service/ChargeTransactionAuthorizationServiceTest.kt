package com.lynas.central.system.service

import com.lynas.central.system.dto.ChargeTransactionStartRequest
import com.lynas.central.system.entity.ChargeTransactionAuthorizationStatus.ALLOWED
import com.lynas.central.system.entity.ChargeTransactionAuthorizationStatus.NOT_ALLOWED
import com.lynas.central.system.entity.DbChargeTransactionAuthorization
import com.lynas.central.system.entity.DbDriverChargePointAccess
import com.lynas.central.system.repository.ChargeTransactionAuthorizationRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.*
import java.net.URI
import java.util.UUID

class ChargeTransactionAuthorizationServiceTest {
    private lateinit var repository: ChargeTransactionAuthorizationRepository
    private lateinit var accessService: DriverChargePointAccessService
    private lateinit var callbackService: CallbackService
    private lateinit var service: ChargeTransactionAuthorizationService

    @BeforeEach
    fun setup() {
        repository = mock()
        accessService = mock()
        callbackService = mock()
        service = ChargeTransactionAuthorizationService(repository, accessService, callbackService)
    }

    @Test
    fun `should save authorization as ALLOWED when access is granted`() {
        val request = ChargeTransactionStartRequest(
            driverId = UUID.randomUUID().toString(),
            stationId = UUID.randomUUID(),
            callBackUrl = URI("https://jsonplaceholder.typicode.com/posts").toURL()
        )
        val driverChargePointAccess = DbDriverChargePointAccess().apply {
            this.driverId = request.driverId
            this.chargePointId = request.stationId
        }
        whenever(accessService.getDriverChargePointAccess(request.driverId, request.stationId))
            .thenReturn(driverChargePointAccess)

        service.createNewChargeTransactionAuthorization(request)

        argumentCaptor<DbChargeTransactionAuthorization>().apply {
            verify(repository).save(capture())
            assert(firstValue.driverId == request.driverId)
            assert(firstValue.chargePointId == request.stationId)
            assert(firstValue.status == ALLOWED.name)
        }

        verify(callbackService).sendAuthorizeChargeTransactionCallbackAsync(
            request,
            ALLOWED.name
        )
    }

    @Test
    fun `should save authorization as NOT_ALLOWED when access is denied`() {
        val request = ChargeTransactionStartRequest(
            driverId = UUID.randomUUID().toString(),
            stationId = UUID.randomUUID(),
            callBackUrl = URI("https://jsonplaceholder.typicode.com/posts").toURL()
        )
        whenever(accessService.getDriverChargePointAccess(request.driverId, request.stationId))
            .thenReturn(null)

        service.createNewChargeTransactionAuthorization(request)

        argumentCaptor<DbChargeTransactionAuthorization>().apply {
            verify(repository).save(capture())
            assert(firstValue.status == NOT_ALLOWED.name)
        }

        verify(callbackService).sendAuthorizeChargeTransactionCallbackAsync(request, NOT_ALLOWED.name)
    }

}