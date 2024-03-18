package com.teamsparta.ecommerce.domain.order.dto

import com.teamsparta.ecommerce.util.enum.PaymentMethod


data class OrderRequestDto(
    var paymentmethod : PaymentMethod,
    var totalprice :Int,
    var items: List<OrderItemRequestDto>,
    val couponId: Long? = null
)

