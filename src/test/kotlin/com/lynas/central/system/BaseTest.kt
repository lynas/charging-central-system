package com.lynas.central.system

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.testcontainers.containers.JdbcDatabaseContainer
import org.testcontainers.containers.PostgreSQLContainerProvider
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers(parallel = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BaseTest {
    lateinit var mockMvc: MockMvc
    @Autowired
    protected lateinit var webApplicationContext: WebApplicationContext
    @BeforeEach
    fun setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }


    companion object {
        private const val DATABASE_NAME = "dev"
        private const val DATABASE_VERSION = "17.4"

        @Container
        private val jdbcDatabaseContainer: JdbcDatabaseContainer<*> =
            PostgreSQLContainerProvider().newInstance(DATABASE_VERSION)
                .let {
                    it.withUsername(DATABASE_NAME)
                    it.withDatabaseName(DATABASE_NAME)
                    it.withPassword(DATABASE_NAME)
                    it.start()
                    it
                }

        /*@Container
        private val rabbitmqContainer: RabbitMQContainer<*> =
            RabbitMQContainer().start()*/

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") {
                "jdbc:postgresql://${jdbcDatabaseContainer.host}" +
                        ":${jdbcDatabaseContainer.firstMappedPort}/${DATABASE_NAME}"
            }
            registry.add("spring.datasource.username") {
                DATABASE_NAME
            }
            registry.add("spring.datasource.password") {
                DATABASE_NAME
            }
        }
    }
}