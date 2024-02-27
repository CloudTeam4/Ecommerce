package com.teamsparta.ecommerce.domain.cart.dto

data class AddItemToCartRequestDto(
    val items : List<Item>
)

data class Item(
    val productId : Long,
    val quantity : Int
)
