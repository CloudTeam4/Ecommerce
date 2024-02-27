package com.teamsparta.ecommerce.domain.cart.repository

import com.teamsparta.ecommerce.domain.cart.model.CartItem
import org.springframework.data.jpa.repository.JpaRepository

interface CartItemRepository : JpaRepository<CartItem, Long> {
}