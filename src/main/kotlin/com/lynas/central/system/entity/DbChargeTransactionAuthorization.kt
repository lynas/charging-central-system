package com.lynas.central.system.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "charge_transaction_authorization")
class DbChargeTransactionAuthorization {
    @Id
    var id : UUID = UUID.randomUUID()
    @Column(name = "DRIVER_ID")
    lateinit var driverId : String
    @Column(name = "CHARGE_POINT_ID")
    lateinit var chargePointId : UUID
    var status : String = ChargeTransactionAuthorizationStatus.UNKNOWN.name
        set(value) {
            field = ChargeTransactionAuthorizationStatus.valueOf(value).name
        }
}

enum class ChargeTransactionAuthorizationStatus{
    ALLOWED, NOT_ALLOWED, UNKNOWN, INVALID
}