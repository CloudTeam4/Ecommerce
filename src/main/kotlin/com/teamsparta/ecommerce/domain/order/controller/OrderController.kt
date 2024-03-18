package com.teamsparta.ecommerce.domain.order.controller

import com.teamsparta.ecommerce.domain.order.dto.CallOrderListDto
import com.teamsparta.ecommerce.domain.order.dto.OrderDetailDto
import com.teamsparta.ecommerce.domain.order.dto.OrderRequestDto
import com.teamsparta.ecommerce.domain.order.dto.OrderResponseDto
import com.teamsparta.ecommerce.domain.order.service.OrderDetailService
import com.teamsparta.ecommerce.domain.order.service.OrderService
import com.teamsparta.ecommerce.security.userdetails.UserDetailsImpl
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal


@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService,
    private val orderDetailService: OrderDetailService
) {

    @PostMapping("/create")
    fun createOrder(@RequestBody orderRequestDto: OrderRequestDto):
            ResponseEntity<OrderResponseDto> {
        val orderResponseDto = orderService.createOrder(orderRequestDto)
        return ResponseEntity.ok(orderResponseDto)
    }

    @DeleteMapping("/{orderId}")
    fun deleteOrder(@PathVariable orderId: Long): ResponseEntity<Void> {
        orderService.deleteOrder(orderId)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{orderId}/cancel")
    fun cancelOrder(@PathVariable orderId: Long): ResponseEntity<Void> {
        orderService.cancelOrder(orderId)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{orderId}/prepare")
    fun orderPreparingProduct(@PathVariable orderId: Long): ResponseEntity<Void> {
        orderService.orderPreparingProduct(orderId)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{orderId}/delivery/start")
    fun orderDeliveryStart(@PathVariable orderId: Long): ResponseEntity<Void> {
        orderService.orderDeliveryStart(orderId)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{orderId}/delivery/ondelivery")
    fun orderOnDelivery(@PathVariable orderId: Long): ResponseEntity<Void> {
        orderService.orderOnDelivery(orderId)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{orderId}/delivery/complete")
    fun orderDeliveryCompleted(@PathVariable orderId: Long): ResponseEntity<Void> {
        orderService.orderDeliveryCompleted(orderId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/List")
    fun findOrderList( @AuthenticationPrincipal member: UserDetailsImpl): ResponseEntity<List<CallOrderListDto>> {
        val callOrderListDto = orderService.findOrderList(member.getMemberId())
        return ResponseEntity.ok(callOrderListDto)
    }

    @GetMapping("/cancel-return-exchange/list")
    fun findOrdersExcludingCancelAndRefund(
        @AuthenticationPrincipal member: UserDetailsImpl
    ): List<CallOrderListDto> {
        return orderService.findOrderCancelAndRefundList(member.getMemberId())
    }

    @GetMapping("/{orderId}/details")
    fun getOrderDetails(@PathVariable orderId: Long): ResponseEntity<List<OrderDetailDto>> {
        val orderDetails = orderDetailService.getOrderDetails(orderId)
        return ResponseEntity.ok(orderDetails)
    }
    @PostMapping("/{orderId}/refund")
    fun refundOrder(@PathVariable orderId: Long): ResponseEntity<String> {
            orderService.refundOrder(orderId)
        return ResponseEntity.ok("환불요청이 완료됨")

    }
}
