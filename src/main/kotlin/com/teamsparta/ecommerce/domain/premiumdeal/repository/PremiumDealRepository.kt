package com.teamsparta.ecommerce.domain.premiumdeal.repository

import com.teamsparta.ecommerce.domain.premiumdeal.model.PremiumDeal
import org.springframework.data.jpa.repository.JpaRepository

interface PremiumDealRepository:JpaRepository<PremiumDeal,Long> {
}