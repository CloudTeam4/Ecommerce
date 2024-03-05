package com.teamsparta.ecommerce.domain.order.service

import com.teamsparta.ecommerce.domain.order.dto.OrderDetailDto
import com.teamsparta.ecommerce.domain.order.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderDetailService(private val orderRepository: OrderRepository) {

    @Transactional
    //Customer 본인확인
    fun getOrderDetails(orderId: Long): List<OrderDetailDto> {
        val order= orderRepository.findById(orderId)
            .orElseThrow { NoSuchElementException("주문을 찾을 수 없습니다.") }

        return order.orderDetails.mapNotNull { orderDetail ->
            val product = orderDetail.product
            product?.let {
                OrderDetailDto(
                    productId = it.itemId!!, // 상품 ID
                    productName = it.name, // 상품명
                    quantity = orderDetail.quantity, // 수량
                    price = it.price // 가격
                )
            }
        }
    }
}
