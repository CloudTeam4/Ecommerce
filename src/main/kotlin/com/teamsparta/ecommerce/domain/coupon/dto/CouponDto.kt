package com.teamsparta.ecommerce.domain.coupon.dto

import com.teamsparta.ecommerce.domain.coupon.model.Coupon

data class CouponDto(

    val couponId: Long? = null,
    var name: String,
    var explanation: String,
    var deductedPrice: Int,
    var status: String,
    var type: Boolean,
    var applicable: String,
    var quantity: Int

) {

    companion object {
        fun fromEntity(coupon: Coupon): CouponDto {
            val dto = CouponDto(
                couponId = coupon.couponId,
                name = coupon.name,
                explanation = coupon.explanation,
                deductedPrice = coupon.deductedPrice,
                status = coupon.status,
                type = coupon.type,
                applicable = coupon.applicable,
                quantity = coupon.quantity
            )
            return dto
        }

        fun fromEntities(coupons: List<Coupon>): List<CouponDto> {
            return coupons.map {
                val dto = CouponDto(
                    couponId = it.couponId,
                    name = it.name,
                    explanation = it.explanation,
                    deductedPrice = it.deductedPrice,
                    status = it.status,
                    type = it.type,
                    applicable = it.applicable,
                    quantity = it.quantity
                )

                dto
            }
        }
    }
}