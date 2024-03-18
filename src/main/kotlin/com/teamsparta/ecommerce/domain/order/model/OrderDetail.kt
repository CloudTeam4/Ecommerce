package com.teamsparta.ecommerce.domain.order.model

import com.teamsparta.ecommerce.domain.product.model.Product
import com.teamsparta.ecommerce.domain.event.model.Event
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
    @JoinColumn(name = "PRODUCT_ID")
    var product: Product? = null,

    @ManyToOne
    @JoinColumn(name = "EVENT_ID", nullable = true)
    var event: Event? = null
){
    fun calculatePrice(): Int {
        // 기본 상품 가격 계산
        val productPrice = product?.price ?: 0
        val totalPrice = productPrice * quantity

        // 특가 상품이면 할인 가격 적용
        return event?.let {
            val discountPrice = it.discountedPrice
            discountPrice * quantity
        } ?: totalPrice
    }
}
