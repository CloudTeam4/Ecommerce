package com.teamsparta.ecommerce.domain.premiumdeal.model

import com.teamsparta.ecommerce.domain.product.model.Product
import jakarta.persistence.*

@Entity
class PremiumDealApply(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    val product: Product,

    @Column(nullable = false)
    val discountRate: Int

)
