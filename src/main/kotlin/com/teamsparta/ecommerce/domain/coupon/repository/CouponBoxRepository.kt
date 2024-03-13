package com.teamsparta.ecommerce.domain.coupon.repository

import com.teamsparta.ecommerce.domain.coupon.model.Coupon
import com.teamsparta.ecommerce.domain.coupon.model.CouponBox
import com.teamsparta.ecommerce.domain.member.model.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CouponBoxRepository: JpaRepository<CouponBox, Long> {
        fun findByMemberAndCoupon(member: Member, coupon: Coupon): Optional<CouponBox>
}

