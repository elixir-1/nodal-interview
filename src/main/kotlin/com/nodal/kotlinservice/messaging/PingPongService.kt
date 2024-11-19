package com.nodal.kotlinservice.messaging

import io.micrometer.core.annotation.Timed
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.Exchange
import org.springframework.amqp.rabbit.annotation.Queue
import org.springframework.amqp.rabbit.annotation.QueueBinding
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

/**
 * PingPongService contains a RabbitListener and Publisher methods for "ping" and "pong" messages.
 * It will consume a "ping" message and publish a "pong"
 * It will wait for a 10 seconds delay and publish a "ping"
 */

@Service
class PingPongService(val rabbitTemplate: RabbitTemplate) {

    companion object {
        // Define the Exchange name and Routing Key for RabbitMQ
        const val JAVA_SERVICE_EXCHANGE_NAME = "com.javaservice.exchange"
        const val KOTLIN_SERVICE_EXCHANGE_NAME = "com.kotlinservice.exchange"
        const val Q2_ROUTING_KEY = "queue2.routing.key"
        const val Q1_ROUTING_KEY = "queue1.routing.key"

        const val PING_MESSAGE = "ping"
        const val PONG_MESSAGE = "pong"

        const val SERVICE_NAME = "kotlin service"

        val log: Logger = LoggerFactory.getLogger(PingPongService::class.java)
    }

    @RabbitListener(bindings = [
        QueueBinding(
            exchange = Exchange(value = KOTLIN_SERVICE_EXCHANGE_NAME, durable = "false"),
            value = Queue(value = "$KOTLIN_SERVICE_EXCHANGE_NAME/$Q2_ROUTING_KEY"),
            key = [Q2_ROUTING_KEY]
        )])
    @Timed
    fun consumeMessage(message: String) {

        log.info("$SERVICE_NAME received message: $message")

        if ((message == PING_MESSAGE)) {
            publishMessage(PONG_MESSAGE)
            try {
                Thread.sleep(10000) // 10-second delay
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
                log.error("Thread interrupted: $e.message")
            }

            publishMessage(PING_MESSAGE)
        }
    }

    fun publishMessage(message: String) {

        rabbitTemplate.convertAndSend(JAVA_SERVICE_EXCHANGE_NAME, Q1_ROUTING_KEY, message)
        log.info("$SERVICE_NAME sent message: $message")
    }
}