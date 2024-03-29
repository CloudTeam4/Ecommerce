package com.teamsparta.ecommerce.domain.order.model

import com.teamsparta.ecommerce.domain.member.model.Member
import com.teamsparta.ecommerce.util.enum.PaymentMethod
import com.teamsparta.ecommerce.util.enum.Status
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "orders")
class Order (

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_METHOD", nullable = false)
    var paymentmethod : PaymentMethod,

    @Column(name = "TOTAL_PRICE")
    var totalprice : Int ? = null,



    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    var status : Status = Status.PAYMENTCOMPLETED,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    // 주문시간
    @Column(name = "ORDER_DATE")
    var orderdate : LocalDateTime,
    //도착시간
    @Column(name = "ARRIVAL_DATE")
    var arrivaldate : LocalDateTime?=null,

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    var member: Member,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    var orderDetails: MutableList<OrderDetail> = mutableListOf()


    ){
    fun addOrderDetail(orderDetail: OrderDetail) {
        orderDetails.add(orderDetail)
        orderDetail.order = this
    }
}