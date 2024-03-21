package com.teamsparta.ecommerce.domain.cart.repository

import com.teamsparta.ecommerce.domain.cart.model.Cart
import com.teamsparta.ecommerce.domain.member.model.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface CartRepository: JpaRepository<Cart, Long> {

}