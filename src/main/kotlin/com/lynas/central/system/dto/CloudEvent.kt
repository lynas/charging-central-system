package com.lynas.central.system.dto

import java.time.LocalDateTime


data class CloudEvent(
    val specversion: String = "1.0",
    val type: String = "com.lynas.central.system.dto.CloudEvent",
    val source: String = "www.cs.com",
    val subject: String,
    val id: String,
    val time: String = LocalDateTime.now().toString(),
    val datacontenttype: String,
    val data: Any,
)