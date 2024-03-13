package com.teamsparta.ecommerce.domain.premiumdeal.service

import com.teamsparta.ecommerce.domain.premiumdeal.dto.PremiumDealApplyRequest
import com.teamsparta.ecommerce.domain.premiumdeal.model.PremiumDeal
import com.teamsparta.ecommerce.domain.premiumdeal.model.PremiumDealApply
import com.teamsparta.ecommerce.domain.premiumdeal.repository.PremiumDealApplyRepository
import com.teamsparta.ecommerce.domain.premiumdeal.repository.PremiumDealRepository
import com.teamsparta.ecommerce.domain.product.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class PremiumDealApplyService(
    private val premiumDealApplyRepository: PremiumDealApplyRepository,
    private val productRepository: ProductRepository,
    private val premiumDealRepository: PremiumDealRepository
) {

    @Transactional
    fun createPremiumDealApply(request: PremiumDealApplyRequest): PremiumDealApply {
        val product = productRepository.findById(request.productId)
            .orElseThrow { IllegalArgumentException("Product not found with id: ${request.productId}") }

        val premiumDealApply = PremiumDealApply(
            product = product,
            discountRate = request.discountRate,
            applicationDate = LocalDateTime.now()
        )

        return premiumDealApplyRepository.save(premiumDealApply)
    }
    @Transactional
    fun createPremiumDeal(apply: PremiumDealApply): PremiumDeal {
        val discountedPrice = calculateDiscountedPrice(apply.product.price, apply.discountRate)
        val premiumDeal = PremiumDeal(
            premiumDealApply = apply,
            discountedPrice = discountedPrice
        )
        return premiumDeal
    }

    @Transactional
    fun save(deal: PremiumDeal): PremiumDeal {
        return premiumDealRepository.save(deal)
    }

    private fun calculateDiscountedPrice(originalPrice: Int, discountRate: Int): Int {
        return originalPrice - (originalPrice * discountRate / 100)
    }
}
