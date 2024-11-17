package com.nodal.kotlinservice.messaging

import io.micrometer.core.annotation.Timed
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
    }

//    @RabbitListener(bindings = [
//        QueueBinding(
//            exchange = Exchange(value = EXCHANGE_NAME, durable = "false"),
//            value = Queue(value = "${EXCHANGE_NAME}/${QUEUE_NAME}"),
//            key = [QUEUE_NAME]
//        )])
    @RabbitListener(queues = [RabbitMQConfig.PING_QUEUE_NAME])
    @Timed
    fun consumePingEvent(message: String) {
        println("Received message: $message")
    }
}