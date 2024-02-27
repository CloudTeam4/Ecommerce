package com.teamsparta.ecommerce.domain.order.controller

import com.teamsparta.ecommerce.domain.order.dto.CallOrderListDto
import com.teamsparta.ecommerce.domain.order.dto.OrderDetailDto
import com.teamsparta.ecommerce.domain.order.dto.OrderRequestDto
import com.teamsparta.ecommerce.domain.order.dto.OrderResponseDto
import com.teamsparta.ecommerce.domain.order.service.OrderDetailService
import com.teamsparta.ecommerce.domain.order.service.OrderService
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity


@RestController
@RequestMapping("/orders")
class OrderController(
     private val orderService: OrderService,
     private val orderDetailService: OrderDetailService) {

    @PostMapping
    fun createOrder(@RequestBody orderRequestDto: OrderRequestDto,
                    @RequestParam memberEmail: String):
            ResponseEntity<OrderResponseDto> {
        val orderResponseDto = orderService.createOrder(orderRequestDto, memberEmail)
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

    @PatchMapping("/{orderId}/delivery/update")
    fun orderOnDelivery(@PathVariable orderId: Long): ResponseEntity<Void> {
        orderService.orderOnDelivery(orderId)
        return ResponseEntity.ok().build()
    }

    @PatchMapping("/{orderId}/delivery/complete")
    fun orderDeliveryCompleted(@PathVariable orderId: Long): ResponseEntity<Void> {
        orderService.orderDeliveryCompleted(orderId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/{memberId}")
    fun findOrderList(@PathVariable memberId: Long): ResponseEntity<CallOrderListDto> {
        val callOrderListDto = orderService.findOrderList(memberId)
        return ResponseEntity.ok(callOrderListDto)
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
