package com.teamsparta.ecommerce.web.request

import jakarta.validation.constraints.NotBlank

data class ProductUpdateRequest(
    var category: Category,

    @field:NotBlank
    var name: String,

    @field:NotBlank
    var explanation: String,

    var price: Int,
) {
}