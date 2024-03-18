package com.teamsparta.ecommerce.domain.event.dto

import com.teamsparta.ecommerce.util.enum.Events

data class EventApplyRequest(
    val productId: Long,
    val discountRate: Int,
    val event: Events
)
