package com.teamsparta.ecommerce.domain.premiumdeal.controller

import com.teamsparta.ecommerce.domain.premiumdeal.dto.PremiumDealApplyRequest
import com.teamsparta.ecommerce.domain.premiumdeal.service.PremiumDealApplyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/premium-deals")
class PremiumDealApplyController(private val premiumDealApplyService: PremiumDealApplyService) {

    @PostMapping("/apply")
    fun createPremiumDealApply(@RequestBody request: PremiumDealApplyRequest): ResponseEntity<Any> {
        val premiumDealApply = premiumDealApplyService.createPremiumDealApply(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(premiumDealApply)
    }
}
