package com.teamsparta.ecommerce.domain.order.dto

import com.teamsparta.ecommerce.domain.order.enum.PaymentMethod
import java.time.LocalDateTime

data class CallOrderListDto(
    var arrivaldate: LocalDateTime,
    var orderdate: LocalDateTime,
    var paymentmethod : PaymentMethod,
    var totalprice :Int
)
