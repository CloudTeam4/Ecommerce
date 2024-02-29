package com.teamsparta.ecommerce.domain.redis.repository

import com.teamsparta.ecommerce.domain.coupon.model.Coupon
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CouponRedisRepository : CrudRepository<Coupon, String> {
}