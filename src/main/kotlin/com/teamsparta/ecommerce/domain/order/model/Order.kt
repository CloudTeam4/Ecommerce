package com.teamsparta.ecommerce.domain.order.model

import com.teamsparta.ecommerce.domain.order.enum.PaymentMethod
import com.teamsparta.ecommerce.domain.order.enum.Status
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime


@Entity
@Table(name = "ORDER")
class Order (

    @Enumerated(EnumType.STRING)
    @Column(name = "PAYMENT_METHOD", nullable = false)
    var paymentmethod : PaymentMethod,

    @Column(name = "TOTAL_PRICE")
    var totalprice : Int,



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
    @JoinColumn(name = "USER_ID")
    var user: User = null,
    ){
}