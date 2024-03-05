package com.teamsparta.ecommerce.util.web.request

import com.teamsparta.ecommerce.util.enum.Category
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