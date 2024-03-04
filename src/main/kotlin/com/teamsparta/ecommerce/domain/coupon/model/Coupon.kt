package com.teamsparta.ecommerce.domain.coupon.model

import com.teamsparta.ecommerce.domain.common.BaseTimeEntity
import com.teamsparta.ecommerce.domain.member.model.Member
import jakarta.persistence.*
import org.springframework.data.redis.core.RedisHash

@Entity
class Coupon(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member,

    @Column(name = "name")
    var name: String,

    @Column(name = "explanation")
    var explanation: String,

    @Column(name = "price")
    var deductedPrice: Int,

    @Column(name = "status")
    var status: String,

    @Column(name = "type")
    var type: Boolean, // 중복 사용 가능 여부

    @Column(name = "statufields")
    var applicable: String, // 적용 가능 대상

) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val couponId: Long? = null

}