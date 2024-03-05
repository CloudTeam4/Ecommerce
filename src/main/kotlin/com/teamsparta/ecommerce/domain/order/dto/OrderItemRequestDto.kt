package com.teamsparta.ecommerce.domain.order.dto

data class OrderItemRequestDto(
    var productId: Long,
    var quantity: Int
)
