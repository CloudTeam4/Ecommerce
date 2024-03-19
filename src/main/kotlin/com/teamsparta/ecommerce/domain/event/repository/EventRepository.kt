package com.teamsparta.ecommerce.domain.event.repository

import com.teamsparta.ecommerce.domain.event.model.Event
import com.teamsparta.ecommerce.util.enum.Events
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface EventRepository:JpaRepository<Event,Long> {

    @Query("SELECT e FROM Event e WHERE e.eventApply.event = :event")
    fun findByEventApply_Event(@Param("event") event: Events): List<Event>

    fun findByEventApplyId(applyId: Long): Event?

}