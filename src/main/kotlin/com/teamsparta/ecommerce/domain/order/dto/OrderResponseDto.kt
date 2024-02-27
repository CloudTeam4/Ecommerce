package com.teamsparta.ecommerce.domain.order.dto

import com.teamsparta.ecommerce.domain.order.enum.PaymentMethod
import com.teamsparta.ecommerce.domain.order.model.Order
import java.time.LocalDateTime

data class OrderResponseDto(
    var orderdate:LocalDateTime,
    var paymentmethod : PaymentMethod,
    var totalprice :Int?
)
