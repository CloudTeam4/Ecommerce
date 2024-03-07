package com.teamsparta.ecommerce.domain.premiumdeal.controller

import com.teamsparta.ecommerce.domain.premiumdeal.model.PremiumDeal
import com.teamsparta.ecommerce.domain.premiumdeal.service.PremiumDealService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/premium-deals")
class PremiumDealController(
    private val premiumDealService: PremiumDealService
) {

    @GetMapping
    fun getAllDeals(): ResponseEntity<List<PremiumDeal>> {
        val deals = premiumDealService.findAllDeals()
        return ResponseEntity.ok(deals)
    }
}
