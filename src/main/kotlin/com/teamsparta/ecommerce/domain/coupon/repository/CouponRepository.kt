package com.teamsparta.ecommerce.domain.coupon.repository

import com.teamsparta.ecommerce.domain.coupon.model.Coupon
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CouponRepository: JpaRepository<Coupon, Long> {
}