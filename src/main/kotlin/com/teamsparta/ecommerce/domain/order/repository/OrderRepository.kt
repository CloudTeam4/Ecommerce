package com.teamsparta.ecommerce.domain.order.repository

import com.teamsparta.ecommerce.domain.order.model.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {
}