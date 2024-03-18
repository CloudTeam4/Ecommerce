package com.teamsparta.ecommerce.domain.event.service

import com.teamsparta.ecommerce.domain.event.model.Event
import com.teamsparta.ecommerce.domain.event.repository.EventRepository
import com.teamsparta.ecommerce.util.enum.Events
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class EventService(
    private val eventRepository: EventRepository
) {

    @Transactional(readOnly = true)
    fun findAllPremiumDeals(): List<Event> {
        return eventRepository.findByEventApply_Event(event = Events.PREMIUMDEAL)
    }
}

