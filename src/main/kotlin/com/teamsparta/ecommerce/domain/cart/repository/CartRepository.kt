package com.teamsparta.ecommerce.domain.cart.repository

import com.teamsparta.ecommerce.domain.cart.model.Cart
import org.springframework.data.jpa.repository.JpaRepository

interface CartRepository: JpaRepository<Cart, Long> {

}