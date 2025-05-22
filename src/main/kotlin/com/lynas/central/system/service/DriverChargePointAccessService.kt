package com.lynas.central.system.service

import com.lynas.central.system.entity.DbDriverChargePointAccess
import com.lynas.central.system.repository.DriverChargePointAccessRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DriverChargePointAccessService(
    private val driverChargePointAccessRepository: DriverChargePointAccessRepository
) {
    fun getDriverChargePointAccess(driverId: String, chargePointId: UUID): DbDriverChargePointAccess? {
        return driverChargePointAccessRepository.findByDriverIdAndChargePointId(driverId, chargePointId)
    }
}