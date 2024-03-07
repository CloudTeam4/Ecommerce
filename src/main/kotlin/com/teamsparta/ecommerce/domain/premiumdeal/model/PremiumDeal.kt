package com.teamsparta.ecommerce.domain.premiumdeal.model

import jakarta.persistence.*



@Entity
class PremiumDeal(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "premiumDealApply_id", referencedColumnName = "id")
    val premiumDealApply: PremiumDealApply,

    @Column(nullable = false)
    val discountedPrice: Int  // 할인가
)

