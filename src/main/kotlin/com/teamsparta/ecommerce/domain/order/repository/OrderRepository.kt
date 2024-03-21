package com.teamsparta.ecommerce.domain.order.repository

import com.teamsparta.ecommerce.domain.order.model.Order
import com.teamsparta.ecommerce.util.enum.Status
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long> {
    fun findByMemberIdAndStatusIn(memberId: Long, statuses: List<Status>): List<Order>
    fun findByMemberIdAndStatusNotIn(memberId: Long, status: Collection<Status>): List<Order>

}