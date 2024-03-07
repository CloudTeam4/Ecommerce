package com.teamsparta.ecommerce.domain.premiumdeal.repository

import com.teamsparta.ecommerce.domain.premiumdeal.model.PremiumDealApply
import org.springframework.data.jpa.repository.JpaRepository

interface PremiumDealApplyRepository:JpaRepository<PremiumDealApply,Long> {
}