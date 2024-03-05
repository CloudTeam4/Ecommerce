package com.teamsparta.ecommerce.domain.product.repository

import com.teamsparta.ecommerce.domain.product.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: JpaRepository<Product, Long> {
}