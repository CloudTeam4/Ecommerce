package com.teamsparta.ecommerce.domain.coupon.model

import com.teamsparta.ecommerce.domain.common.BaseTimeEntity
import jakarta.persistence.*
import org.springframework.data.redis.core.RedisHash

@Entity
@RedisHash(value = "coupon", timeToLive = 100)
class Coupon(

    @Column(name = "name")
    var name: String,

    @Column(name = "explanation")
    var explanation: String,

    @Column(name = "price")
    var deductedPrice: Int,

    @Column(name = "status")
    var status: String,

    @Column(name = "type")
    var type: String, // 할인방식

    @Column(name = "statufields")
    var field: String, // 사용 가능 형식

) : BaseTimeEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val couponId: Long? = null
}