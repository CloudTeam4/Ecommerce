package com.teamsparta.ecommerce.domain.premiumdeal.dto

data class PremiumDealApplyRequest(
    val productId: Long,
    val discountRate: Int
)
