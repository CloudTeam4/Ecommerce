package com.teamsparta.ecommerce.domain.premiumdeal.service

import com.teamsparta.ecommerce.domain.premiumdeal.model.PremiumDeal
import com.teamsparta.ecommerce.domain.premiumdeal.repository.PremiumDealRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PremiumDealService(
    private val premiumDealRepository: PremiumDealRepository
) {

    @Transactional(readOnly = true)
    fun findAllDeals(): List<PremiumDeal> {
        return premiumDealRepository.findAll()
    }
}

