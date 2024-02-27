package com.teamsparta.ecommerce.domain.cart.dto

data class DeleteItemFromCartRequestDto(
    val productIdList: List<Long>
)
