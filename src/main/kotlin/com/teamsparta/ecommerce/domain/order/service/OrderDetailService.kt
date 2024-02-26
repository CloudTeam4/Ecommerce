package com.teamsparta.ecommerce.domain.order.service

import com.teamsparta.ecommerce.domain.order.dto.CallOrderListDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderDetailService {

    @Transactional
    fun FindOrderList(userId:Long): CallOrderListDto {
        val order = orderRepository.findById((userId)).orElseThrow()

        return CallOrderListDto(
            orderdate = order.orderdate,
            arrivaldate = order.arrivaldate,
            paymentmethod = order.paymentmethod,
            totalprice = order.totalprice
        )
}
}