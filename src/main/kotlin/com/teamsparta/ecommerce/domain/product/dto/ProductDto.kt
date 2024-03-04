package com.teamsparta.ecommerce.domain.product.dto

import com.teamsparta.ecommerce.domain.product.model.Product
import com.teamsparta.ecommerce.util.enum.Category

data class ProductDto(

    val itemId: Long? = null,
    var category: Category,
    var name: String,
    var explanation: String,
    var price: Int

) {

    companion object {
        fun fromEntity(product: Product): ProductDto {
            val dto = ProductDto(
                itemId = product.itemId,
                category = product.category,
                name = product.name,
                explanation = product.explanation,
                price = product.price
            )
            return dto
        }

        fun fromEntities(menus: List<Product>): List<ProductDto> {
            return menus.map {
                val dto = ProductDto(
                    itemId = it.itemId,
                    category = it.category,
                    name = it.name,
                    explanation = it.explanation,
                    price = it.price
                )

                dto
            }
        }
    }
}