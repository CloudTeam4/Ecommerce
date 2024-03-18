package com.teamsparta.ecommerce.domain.event.model

import jakarta.persistence.*



@Entity
class Event(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "EventApply_id", referencedColumnName = "id")
    val eventApply: EventApply,

    @Column(nullable = false)
    val discountedPrice: Int  // 할인가
)

