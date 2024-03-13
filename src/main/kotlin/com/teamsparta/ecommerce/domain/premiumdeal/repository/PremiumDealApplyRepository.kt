package com.teamsparta.ecommerce.domain.premiumdeal.repository

import com.teamsparta.ecommerce.domain.premiumdeal.model.PremiumDealApply

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime


@Repository
interface PremiumDealApplyRepository:JpaRepository<PremiumDealApply,Long> {
    @Query("SELECT pda FROM PremiumDealApply pda WHERE pda.applicationDate > :startTime AND pda.applicationDate <= :endTime")
    fun findByApplicationDateBetween(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        pageable: Pageable
    ): Page<PremiumDealApply>





}