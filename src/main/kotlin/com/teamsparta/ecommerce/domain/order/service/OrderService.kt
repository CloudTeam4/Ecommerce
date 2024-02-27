package com.teamsparta.ecommerce.domain.order.service

import com.teamsparta.ecommerce.domain.order.dto.CallOrderListDto
import com.teamsparta.ecommerce.domain.order.dto.OrderRequestDto
import com.teamsparta.ecommerce.domain.order.dto.OrderResponseDto
import com.teamsparta.ecommerce.domain.order.enum.Status
import com.teamsparta.ecommerce.domain.order.model.Order
import com.teamsparta.ecommerce.domain.order.repository.OrderRepository

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {

    @Transactional
    fun createOrder(orderRequestdto: OrderRequestDto): OrderResponseDto {
        val authentication = SecurityContextHolder.getContext().authentication
        val userEmail = authentication.name
        val user = userRepository.findByUserEmail(userEmail)
            ?: throw UsernameNotFoundException("Passenger not found with email: $userEmail")

        val order = Order(
            orderdate = LocalDateTime.now(),
            paymentmethod = orderRequestdto.paymentmethod,
            totalprice = orderRequestdto.totalprice,
            user = user
        )
        return OrderResponseDto(
            orderdate = order.orderdate,
            paymentmethod = order.paymentmethod,
            totalprice = order.totalprice,
        )

    }

    @Transactional
    fun deleteOrder(orderId: Long) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if ((order.status == Status.ORDERCANCEL) || (order.status == Status.DELIVERYCOMPLETED))
            orderRepository.deleteById(orderId)
    }

    @Transactional
    fun cancelOrder(orderId: Long) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if (order.status == Status.PAYMENTCOMPLETED)
            order.status == Status.ORDERCANCEL
    }


    @Transactional
    fun OrderPreparingProduct(orderId: Long) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if (order.status == Status.PAYMENTCOMPLETED)
            order.status = Status.PRIPAIRINGPRODUCT
    }
    @Transactional
    fun OrderDeliveryStart(orderId: Long) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if (order.status == Status.PRIPAIRINGPRODUCT)
            order.status = Status.DELIVERYSTARTED
    }

    @Transactional
    fun OrderOnDelivery(orderId: Long) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if (order.status == Status.DELIVERYSTARTED)
            order.status = Status.ONDELIVERY
    }

    @Transactional
    fun OrderDeliveryCompleted(orderId: Long) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if (order.status == Status.ONDELIVERY)
            order.status = Status.DELIVERYCOMPLETED
            order.arrivaldate = LocalDateTime.now()
    }

    @Transactional
    fun FindOrderList(userId:Long): CallOrderListDto{
        val order = orderRepository.findById((userId)).orElseThrow()

        return CallOrderListDto(
            orderdate = order.orderdate,
            arrivaldate = order.arrivaldate,
            paymentmethod = order.paymentmethod,
            totalprice = order.totalprice
        )



                }


    }
