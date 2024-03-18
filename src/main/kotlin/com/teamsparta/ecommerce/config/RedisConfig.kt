package com.teamsparta.ecommerce.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.transaction.annotation.EnableTransactionManagement


@Configuration
class RedisConfig {
    @Value("\${spring.data.redis.host}")
    private val host: String? = null

    @Value("\${spring.data.redis.port}")
    private val port = 0

    @Bean(destroyMethod = "shutdown")
    fun redissonClient() : RedissonClient {
        val config = Config()
        config.useSingleServer() // Redis 클라이언트가 단일 Redis 서버에 연결하도록 구성
            .setAddress("redis://$host:$port") // 연결할 Redis 서버의 주소를 설정
            .setDnsMonitoringInterval(-1) //DNS 모니터링 간격 설정, 여기서는 DNS 모니터링을 사용하지 않도록 -1로 설정
        return Redisson.create(config)
    }

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        return LettuceConnectionFactory(host!!, port)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.connectionFactory = redisConnectionFactory()

        // 일반적인 key:value의 경우 시리얼라이저
        redisTemplate.keySerializer = StringRedisSerializer()
        redisTemplate.valueSerializer = StringRedisSerializer()

        // Hash를 사용할 경우 시리얼라이저
        redisTemplate.hashKeySerializer = StringRedisSerializer()
        redisTemplate.hashValueSerializer = StringRedisSerializer()

        // 모든 경우
        redisTemplate.setDefaultSerializer(StringRedisSerializer())

        return redisTemplate
    }
}