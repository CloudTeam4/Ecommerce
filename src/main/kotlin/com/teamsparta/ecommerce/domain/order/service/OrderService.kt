package com.teamsparta.ecommerce.domain.order.service

import com.teamsparta.ecommerce.domain.member.repository.MemberRepository
import com.teamsparta.ecommerce.domain.order.dto.CallOrderListDto
import com.teamsparta.ecommerce.domain.order.dto.OrderRequestDto
import com.teamsparta.ecommerce.domain.order.dto.OrderResponseDto
import com.teamsparta.ecommerce.domain.order.model.Order
import com.teamsparta.ecommerce.domain.order.model.OrderDetail
import com.teamsparta.ecommerce.domain.order.repository.OrderRepository
import com.teamsparta.ecommerce.domain.product.repository.ProductRepository
import com.teamsparta.ecommerce.util.enum.Status
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val memberRepository: MemberRepository,
    private val productRepository: ProductRepository
) {

    @Transactional
    fun createOrder(orderRequestdto: OrderRequestDto, memberEmail: String): OrderResponseDto {
        val member = memberRepository.findByEmail(memberEmail).orElseThrow()
        val order = Order(
            orderdate = LocalDateTime.now(),
            paymentmethod = orderRequestdto.paymentmethod,
            member = member
        )
        val orderDetails = mutableListOf<OrderDetail>()
        var totalPrice = 0 // 총 주문 가격 초기화
        for (item in orderRequestdto.items) {
            val product = productRepository.findById(item.productId).orElseThrow()
            val productPrice = product.price
                totalPrice += (productPrice * item.quantity)
        }
        order.orderDetails.addAll(orderDetails)
        order.totalprice = totalPrice
        val savedOrder = orderRepository.save(order)
        return OrderResponseDto(
            orderdate = savedOrder.orderdate,
            paymentmethod = savedOrder.paymentmethod,
            totalprice = savedOrder.totalprice
        )
    }

    @Transactional
    fun deleteOrder(orderId: Long) {
        val authentication = SecurityContextHolder.getContext().authentication
        val memberEmail = authentication.name
        val order = orderRepository.findById(orderId).orElseThrow()
        if ((order.member.email == memberEmail)&&((order.status == Status.ORDERCANCEL) || (order.status == Status.DELIVERYCOMPLETED)))
            orderRepository.deleteById(orderId)
    }

    @Transactional
    fun cancelOrder(orderId: Long) {
        val authentication = SecurityContextHolder.getContext().authentication
        val memberEmail = authentication.name
        val order = orderRepository.findById(orderId).orElseThrow()
        if ((order.member.email == memberEmail)&&(order.status == Status.PAYMENTCOMPLETED||order.status == Status.PRIPAIRINGPRODUCT))
            order.status = Status.ORDERCANCEL
    }
    @Transactional
    fun refundOrder(orderId: Long) {
        val authentication = SecurityContextHolder.getContext().authentication
        val memberEmail = authentication.name
        val order = orderRepository.findById(orderId).orElseThrow()
        val now = LocalDateTime.now()
        val duration = Duration.between(order.orderdate, now)
        val hoursDifference = duration.toHours()
        if(order.member.email == memberEmail){
            if (hoursDifference <= 168) { // 168시간(7일) 이내
                order.status = Status.ORDERREFUND
            } else {
                throw IllegalStateException("7일 이내에만 반품 할 수 있습니다.")
            }
        }
    }

    @Transactional
    fun orderPreparingProduct(orderId: Long) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if (order.status == Status.PAYMENTCOMPLETED)
            order.status = Status.PRIPAIRINGPRODUCT
    }
    @Transactional
    fun orderDeliveryStart(orderId: Long) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if (order.status == Status.PRIPAIRINGPRODUCT)
            order.status = Status.DELIVERYSTARTED
    }

    @Transactional
    fun orderOnDelivery(orderId: Long) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if (order.status == Status.DELIVERYSTARTED)
            order.status = Status.ONDELIVERY
    }

    @Transactional
    fun orderDeliveryCompleted(orderId: Long) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if (order.status == Status.ONDELIVERY)
            order.status = Status.DELIVERYCOMPLETED
            order.arrivaldate = LocalDateTime.now()
    }

    @Transactional
    fun findOrderList(memberId: Long): List<CallOrderListDto> {
        val excludedStatuses = listOf(Status.ORDERCANCEL, Status.ORDERREFUND)
        val orders = orderRepository.findByMemberIdAndStatusNotIn(memberId, excludedStatuses)
        return orders.map { order ->
            CallOrderListDto(
                orderdate = order.orderdate,
                arrivaldate = order.arrivaldate,
                paymentmethod = order.paymentmethod,
                totalprice = order.totalprice
            )
        }
    }

    @Transactional
    fun findOrdercancelandrefundList(memberId: Long): List<CallOrderListDto> {
        val statuses = listOf(Status.ORDERCANCEL, Status.ORDERREFUND)
        val orders = orderRepository.findByMemberIdAndStatusIn(memberId, statuses)
        return orders.map { order ->
            CallOrderListDto(
                orderdate = order.orderdate,
                arrivaldate = order.arrivaldate,
                paymentmethod = order.paymentmethod,
                totalprice = order.totalprice
            )
        }
    }

}
