package com.teamsparta.ecommerce.domain.coupon.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class CouponCountRepository( // Redis를 사용하여 쿠폰 카운트를 저장하고 관리하는 Repository
    private val redisTemplate: RedisTemplate<String, String>
) {

    companion object {
        private const val COUPON_COUNT_KEY = "coupon_count"
        //private const val MAX_COUPON_COUNT = 100
    }

    // 쿠폰 수 증가
    fun increment(): Long {
        return redisTemplate.opsForValue().increment(COUPON_COUNT_KEY) ?: 0
    }

    // 현재 쿠폰 수
    fun getCount(): Long {
        return redisTemplate.opsForValue().get(COUPON_COUNT_KEY)?.toLong() ?: 0
    }
}