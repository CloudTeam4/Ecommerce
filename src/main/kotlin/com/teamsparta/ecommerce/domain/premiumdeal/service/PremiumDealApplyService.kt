package com.teamsparta.ecommerce.domain.premiumdeal.service

import com.teamsparta.ecommerce.domain.premiumdeal.dto.PremiumDealApplyRequest
import com.teamsparta.ecommerce.domain.premiumdeal.model.PremiumDealApply
import com.teamsparta.ecommerce.domain.premiumdeal.repository.PremiumDealApplyRepository
import com.teamsparta.ecommerce.domain.product.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PremiumDealApplyService(
    private val premiumDealApplyRepository: PremiumDealApplyRepository,
    private val productRepository: ProductRepository
) {

    @Transactional
    fun createPremiumDealApply(request: PremiumDealApplyRequest): PremiumDealApply {
        val product = productRepository.findById(request.productId)
            .orElseThrow { IllegalArgumentException("Product not found with id: ${request.productId}") }

        val premiumDealApply = PremiumDealApply(
            product = product,
            discountRate = request.discountRate
        )

        return premiumDealApplyRepository.save(premiumDealApply)
    }
}
