package com.lynas.central.system.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Configuration
class AppConfig {

    @Bean
    fun executor(): ExecutorService {
        return Executors.newVirtualThreadPerTaskExecutor()
    }
}