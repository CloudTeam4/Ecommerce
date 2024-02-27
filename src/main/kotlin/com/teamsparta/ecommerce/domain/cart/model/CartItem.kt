package com.teamsparta.ecommerce.domain.cart.model

import com.teamsparta.ecommerce.domain.product.model.Product
import jakarta.persistence.*

@Entity
data class CartItem(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "cart_id")
    var cart: Cart,

    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product,

    @Column(nullable = false)
    var quantity : Int

)