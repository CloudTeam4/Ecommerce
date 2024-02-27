package com.teamsparta.ecommerce.domain.order.repository

import com.teamsparta.ecommerce.domain.order.model.Order
import org.springframework.data.repository.CrudRepository

interface OrderRepository : CrudRepository<Order, Long> {
}