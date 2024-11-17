package com.nodal.kotlinservice.messaging

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMQConfig {

    companion object {
        const val EXCHANGE_NAME = "com.kotlinservice"
        const val PING_QUEUE_NAME = "Pingdom"
        const val PONG_QUEUE_NAME = "Pongdom"
    }


    @Bean
    fun exchange(): DirectExchange = DirectExchange(EXCHANGE_NAME, true, false)

    @Bean
    fun pingQueue(): Queue = Queue(PING_QUEUE_NAME, false)

    @Bean
    fun pongQueue(): Queue = Queue(PONG_QUEUE_NAME, false)

    @Bean
    fun bindingPing(pingQueue: Queue, exchange: DirectExchange): Binding =
        BindingBuilder.bind(pingQueue).to(exchange).with("${EXCHANGE_NAME}/${PING_QUEUE_NAME}")

    @Bean
    fun bindingPong(pongQueue: Queue, exchange: DirectExchange): Binding =
        BindingBuilder.bind(pongQueue).to(exchange).with("${EXCHANGE_NAME}/${PONG_QUEUE_NAME}")
}
