package com.lynas.central.system

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@EnableTransactionManagement
@SpringBootApplication
class CentralSystemApplication

fun main(args: Array<String>) {
	runApplication<CentralSystemApplication>(*args)
}
