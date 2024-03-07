package com.teamsparta.ecommerce.domain.cart.model

import jakarta.persistence.*

@Entity
data class Cart(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToMany(mappedBy = "cart", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var cartItemList: MutableList<CartItem> = mutableListOf(),

    @Column(nullable = false)
    var totalQuantity : Int ?= 0

)