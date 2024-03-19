package com.teamsparta.ecommerce.domain.event.repository

import com.teamsparta.ecommerce.domain.event.model.EventApply
import com.teamsparta.ecommerce.domain.product.model.Product
import com.teamsparta.ecommerce.util.enum.Events

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime


@Repository
interface EventApplyRepository:JpaRepository<EventApply,Long> {

    fun findByProductItemId(itemId: Long): List<EventApply>?

    fun existsByProductAndEvent(product: Product, event: Events): Boolean

    @Query("SELECT pda FROM EventApply pda WHERE pda.applicationDate > :startTime AND pda.applicationDate <= :endTime")
    fun findByApplicationDateBetween(
        startTime: LocalDateTime,
        endTime: LocalDateTime,
        pageable: Pageable
    ): Page<EventApply>






}