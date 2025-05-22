package com.lynas.central.system.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig {
    @Bean
    fun restClient(builder: RestClient.Builder): RestClient {
        return builder
            .requestFactory(getClientHttpRequestFactory())
            .build()
    }

    private fun getClientHttpRequestFactory(): ClientHttpRequestFactory {
        val factory = SimpleClientHttpRequestFactory()
        factory.setReadTimeout(6000)
        factory.setConnectTimeout(6000)
        return factory
    }
}