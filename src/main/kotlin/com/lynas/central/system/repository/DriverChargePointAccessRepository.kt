package com.lynas.central.system.repository

import com.lynas.central.system.entity.DbDriverChargePointAccess
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DriverChargePointAccessRepository: JpaRepository<DbDriverChargePointAccess, UUID> {
    fun findByDriverIdAndChargePointId(driverId: String, chargepointId: UUID): DbDriverChargePointAccess?
}