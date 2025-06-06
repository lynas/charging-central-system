CREATE TABLE IF NOT EXISTS DRIVER_CHARGE_POINT_ACCESS
(
    ID                                 UUID      NOT NULL,
    CHARGE_POINT_ID                    UUID      NOT NULL,
    DRIVER_ID                          TEXT      NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT UQ_CHARGE_POINT_DRIVER UNIQUE (CHARGE_POINT_ID, DRIVER_ID)
);

CREATE TABLE IF NOT EXISTS CHARGE_TRANSACTION_AUTHORIZATION
(
    ID                UUID        NOT NULL,
    DRIVER_ID         TEXT        NOT NULL,
    CHARGE_POINT_ID   UUID        NOT NULL,
    STATUS            TEXT        NOT NULL DEFAULT 'UNKNOWN',
    PRIMARY KEY (ID),
    CONSTRAINT CHK_CHARGE_TRANSACTION_STATUS CHECK (STATUS IN ('ALLOWED', 'NOT_ALLOWED', 'UNKNOWN', 'INVALID'))
);