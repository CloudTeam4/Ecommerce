package com.teamsparta.ecommerce.util.rabbit

import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.core.AmqpTemplate
import org.springframework.amqp.core.Queue
import org.springframework.stereotype.Component

@Component
class RabbitService(private val amqpAdmin: AmqpAdmin, private val amqpTemplate: AmqpTemplate) {
    fun sendMessage(couponId: String, memberId: String) {
        if(amqpAdmin.getQueueInfo("coupon")==null){
            amqpAdmin.declareQueue(Queue("coupon"))
        }
        val messageContent = "$couponId $memberId"
        amqpTemplate.convertAndSend("coupon", messageContent)
        println("Message sent to 'coupon' queue: $messageContent")
    }

}