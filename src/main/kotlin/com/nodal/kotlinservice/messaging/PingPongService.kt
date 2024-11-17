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

@Service
class PingPongService(val rabbitTemplate: RabbitTemplate) {

    companion object {
        const val EXCHANGE_NAME = "com.kotlinservice"
        const val QUEUE_NAME = "PingPong"

        val log: Logger = LoggerFactory.getLogger(PingPongService::class.java)
    }

    @RabbitListener(bindings = [
        QueueBinding(
            exchange = Exchange(value = EXCHANGE_NAME, durable = "false"),
            value = Queue(value = "${EXCHANGE_NAME}/${QUEUE_NAME}"),
            key = [QUEUE_NAME]
        )])
    @Timed
    fun consumePingEvent(message: String) {
//        println("Received message: $message")
        log.info("Received message: $message")

        Thread.sleep(10000)
        rabbitTemplate.convertAndSend(
            EXCHANGE_NAME,
            PONG_QUEUE_NAME,
            "pong"
        )
    }
}