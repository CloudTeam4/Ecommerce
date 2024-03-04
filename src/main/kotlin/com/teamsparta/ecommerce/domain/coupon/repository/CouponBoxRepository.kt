package com.teamsparta.ecommerce.domain.coupon.repository

import com.teamsparta.ecommerce.domain.coupon.model.CouponBox
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CouponBoxRepository: JpaRepository<CouponBox, Long> {
}