package com.teamsparta.ecommerce.domain.cart.model

import com.teamsparta.ecommerce.domain.member.model.Member
import jakarta.persistence.*

@Entity
data class Cart(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "member_id")
    var member: Member,

    @OneToMany(mappedBy = "cart", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var cartItemList: MutableList<CartItem> = mutableListOf(),

    @Column(nullable = false)
    var totalQuantity : Int ?= 0

)