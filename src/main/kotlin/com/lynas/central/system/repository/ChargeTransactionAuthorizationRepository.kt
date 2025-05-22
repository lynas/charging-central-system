package com.lynas.central.system.repository

import com.lynas.central.system.entity.DbChargeTransactionAuthorization
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ChargeTransactionAuthorizationRepository: JpaRepository<DbChargeTransactionAuthorization, UUID> {
}