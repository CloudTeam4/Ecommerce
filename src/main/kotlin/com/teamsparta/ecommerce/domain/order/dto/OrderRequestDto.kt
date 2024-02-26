package com.teamsparta.ecommerce.domain.order.dto

import com.teamsparta.ecommerce.domain.order.enum.PaymentMethod

data class OrderRequestDto(
    var paymentmethod : PaymentMethod,
    var totalprice :Int
)
