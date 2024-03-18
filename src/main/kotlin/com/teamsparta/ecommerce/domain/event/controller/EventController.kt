package com.teamsparta.ecommerce.domain.event.controller

import com.teamsparta.ecommerce.domain.event.model.Event
import com.teamsparta.ecommerce.domain.event.service.EventService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/event")
class EventController(
    private val eventService: EventService
) {

    @GetMapping("/premium_deal")
    fun getAllDeals(): ResponseEntity<List<Event>> {
        val deals = eventService.findAllPremiumDeals()
        return ResponseEntity.ok(deals)
    }
}
