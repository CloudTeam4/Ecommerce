package com.teamsparta.ecommerce.domain.event.model

import com.teamsparta.ecommerce.domain.product.model.Product
import com.teamsparta.ecommerce.util.enum.Events
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class EventApply(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    val product: Product,

    @Column(nullable = false)
    val discountRate: Int,

    @Enumerated(EnumType.STRING)
    var event : Events,

    @Column(nullable = false)
    val applicationDate: LocalDateTime

)
