package com.teamsparta.ecommerce.domain.premiumdeal.repository

import com.teamsparta.ecommerce.domain.premiumdeal.model.PremiumDeal
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PremiumDealRepository : JpaRepository<PremiumDeal, Long>{

    fun findByPremiumDealApplyId(applyId: Long): PremiumDeal?


}