package com.teamsparta.ecommerce.util.rabbit

import org.springframework.amqp.core.AcknowledgeMode
import org.springframework.amqp.rabbit.annotation.EnableRabbit
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableRabbit
class RabbitConfig(
    @Value("\${spring.rabbitmq.username}")
    private val username : String,
    @Value("\${spring.rabbitmq.password}")
    private val password : String,
    @Value("\${spring.rabbitmq.host}")
    private val host : String,
    @Value("\${spring.rabbitmq.port}")
    private val port : String

) {
    fun myFactory(): CachingConnectionFactory {
        val factory = CachingConnectionFactory()
        factory.username = username
        factory.setPassword(password)
        factory.setHost(host)
        factory.port = port.toInt()
        return factory
    }

    @Bean
    fun myRabbitListenerContainerFactory(): SimpleRabbitListenerContainerFactory {
        val factory = SimpleRabbitListenerContainerFactory()
        factory.setConnectionFactory(myFactory())
        factory.setMaxConcurrentConsumers(10)
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO)
        return factory
    }
}