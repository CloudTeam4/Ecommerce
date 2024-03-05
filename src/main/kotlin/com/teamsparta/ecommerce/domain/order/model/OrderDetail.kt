package com.teamsparta.ecommerce.domain.order.model

import com.teamsparta.ecommerce.domain.product.model.Product
import jakarta.persistence.*

@Entity
@Table(name = "ORDER_DETAIL")
class OrderDetail (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "QUANTITY")
    var quantity: Int,

    @ManyToOne
    @JoinColumn(name = "ORDER_ID")
    var order: Order? = null,

    @ManyToOne
    @JoinColumn(name = "ITEM_ID")
    var product: Product? = null,

){


}