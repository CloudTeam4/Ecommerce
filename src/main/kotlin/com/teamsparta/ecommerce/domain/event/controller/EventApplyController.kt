package com.teamsparta.ecommerce.domain.event.controller

import com.teamsparta.ecommerce.domain.event.dto.EventApplyRequest
import com.teamsparta.ecommerce.domain.event.service.EventApplyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/event/apply")
class EventApplyController(private val eventApplyService: EventApplyService) {

    @PostMapping("/create")
    fun createEventApply(@RequestBody request: EventApplyRequest): ResponseEntity<Any> {
        val eventApply = eventApplyService.createEventApply(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(eventApply)
    }
}
