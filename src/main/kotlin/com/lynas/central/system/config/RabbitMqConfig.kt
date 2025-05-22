package com.lynas.central.system.config

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor


@Configuration
class RabbitMqConfig {

    // Main Exchange
    @Bean
    fun exchange(): TopicExchange = TopicExchange(EXCHANGE_CHARGE_TRANSACTION)

    // Dead Letter Exchange
    @Bean
    fun deadLetterExchange(): TopicExchange = TopicExchange(DLX_CHARGE_TRANSACTION)

    // Dead Letter Queue
    @Bean
    fun deadLetterQueue(): Queue = Queue(DLQ_CHARGE_TRANSACTION_START)

    // Dead Letter Binding
    @Bean
    fun deadLetterBinding(): Binding =
        BindingBuilder.bind(deadLetterQueue())
            .to(deadLetterExchange())
            .with(DLX_ROUTE_KEY_CHARGE_TRANSACTION_START)

    // Main Queue with DLQ settings
    @Bean
    fun queue(): Queue =
        Queue(
            QUEUE_CHARGE_TRANSACTION_START,
            true,
            false,
            false,
            mapOf(
                "x-dead-letter-exchange" to DLX_CHARGE_TRANSACTION,
                "x-dead-letter-routing-key" to DLX_ROUTE_KEY_CHARGE_TRANSACTION_START
            )
        )

    // Binding Main Queue to Main Exchange
    @Bean
    fun binding(): Binding =
        BindingBuilder.bind(queue())
            .to(exchange())
            .with(ROUTE_KEY_CHARGE_TRANSACTION_START)

    @Bean
    fun template(connectionFactory: ConnectionFactory) = RabbitTemplate(connectionFactory).apply {
        messageConverter = Jackson2JsonMessageConverter()
    }

    @Bean
    fun listenerContainerFactory(connectionFactory: ConnectionFactory, virtualThreadExecutor: TaskExecutor) =
        SimpleRabbitListenerContainerFactory().apply {
            setConnectionFactory(connectionFactory)
            setTaskExecutor(virtualThreadExecutor)
            setMessageConverter(Jackson2JsonMessageConverter())
            setDefaultRequeueRejected(false) // if error happen send message to DLQ
        }

    @Bean
    fun virtualThreadExecutor(): TaskExecutor {
        return TaskExecutor { runnable ->
            Thread.ofVirtual().start(runnable)
        }
    }

}

const val EXCHANGE_CHARGE_TRANSACTION = "EXCHANGE_CHARGE_TRANSACTION"
const val QUEUE_CHARGE_TRANSACTION_START = "QUEUE_CHARGE_TRANSACTION_START"
const val ROUTE_KEY_CHARGE_TRANSACTION_START = "ROUTE_KEY_CHARGE_TRANSACTION_START"

const val DLX_CHARGE_TRANSACTION = "DLX_CHARGE_TRANSACTION"
const val DLQ_CHARGE_TRANSACTION_START = "DLQ_CHARGE_TRANSACTION_START"
const val DLX_ROUTE_KEY_CHARGE_TRANSACTION_START = "DLX_ROUTE_KEY_CHARGE_TRANSACTION_START"

