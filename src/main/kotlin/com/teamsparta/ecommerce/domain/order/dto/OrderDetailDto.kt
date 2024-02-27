package com.teamsparta.ecommerce.domain.order.dto

data class OrderDetailDto(
    val productId: Long?, // 상품 ID
    val productName: String, // 상품명
    val quantity: Int, // 수량
    val price: Int // 가격
)