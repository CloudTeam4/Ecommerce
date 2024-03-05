package com.teamsparta.ecommerce.util.web.request

import jakarta.validation.constraints.NotBlank

data class CouponCreateRequest(

    @field:NotBlank
    var name: String,

    @field:NotBlank
    var explanation: String,

    var deductedPrice: Int,

    var status: String,

    var type: Boolean, // 중복 사용 가능 여부

    var applicable: String, // 적용 가능 대상
) {
}