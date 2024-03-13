package com.teamsparta.ecommerce.domain.cart.dto

data class UpdateProductQuantityRequestDto(
    val productId : Long,
    val quantity : Int
)
