package com.teamsparta.ecommerce.domain.cart.repository

import com.teamsparta.ecommerce.domain.cart.model.Cart
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface CartRepository: JpaRepository<Cart, Long> {
    fun findByMemberId(memberId: Long) : Optional<Cart>
}