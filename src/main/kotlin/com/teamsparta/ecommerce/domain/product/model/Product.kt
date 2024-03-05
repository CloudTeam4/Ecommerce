package com.teamsparta.ecommerce.domain.product.model

import com.teamsparta.ecommerce.util.enum.Category
import jakarta.persistence.*

@Entity
class Product(

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    var category: Category,

    @Column(name = "name")
    var name: String,

    @Column(name = "explanation")
    var explanation: String,

    @Column(name = "price")
    var price: Int,

    @Column(name = "image")
    var imageUrl: String

) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val itemId: Long? = null

    fun update(category: Category, name: String, explanation: String, price: Int) {
        this.category = category
        this.name = name
        this.explanation = explanation
        this.price = price
    }
}