package com.teamsparta.ecommerce.domain.coupon.model

import com.teamsparta.ecommerce.domain.member.model.Member
import jakarta.persistence.*

@Entity
class CouponBox(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    val coupon: Coupon

) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val couponBoxId: Long? = null

}