package com.lynas.central.system.service

import com.lynas.central.system.dto.ChargeTransactionStartRequest
import com.lynas.central.system.entity.ChargeTransactionAuthorizationStatus.ALLOWED
import com.lynas.central.system.entity.ChargeTransactionAuthorizationStatus.NOT_ALLOWED
import com.lynas.central.system.entity.DbChargeTransactionAuthorization
import com.lynas.central.system.repository.ChargeTransactionAuthorizationRepository
import org.springframework.stereotype.Service


@Service
class ChargeTransactionAuthorizationService(
    private val chargeTransactionAuthorizationRepository: ChargeTransactionAuthorizationRepository,
    private val driverChargePointAccessService: DriverChargePointAccessService,
    private val callbackService: CallbackService
) {

    fun createNewChargeTransactionAuthorization(startRequest: ChargeTransactionStartRequest) {
        val driverChargePointAccess =
            driverChargePointAccessService.getDriverChargePointAccess(startRequest.driverId, startRequest.stationId)
        val status = if (driverChargePointAccess != null) ALLOWED.name else NOT_ALLOWED.name
        val chargeTransactionAuthorization = DbChargeTransactionAuthorization().apply {
            this.driverId = startRequest.driverId
            this.chargePointId = startRequest.stationId
            this.status = status
        }
        chargeTransactionAuthorizationRepository.save(chargeTransactionAuthorization)
        callbackService.sendAuthorizeChargeTransactionCallbackAsync(startRequest, status)
    }
}
