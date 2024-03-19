package com.teamsparta.ecommerce.domain.event.service

import com.teamsparta.ecommerce.domain.event.dto.EventApplyRequest
import com.teamsparta.ecommerce.domain.event.model.Event
import com.teamsparta.ecommerce.domain.event.model.EventApply
import com.teamsparta.ecommerce.domain.event.repository.EventApplyRepository
import com.teamsparta.ecommerce.domain.event.repository.EventRepository
import com.teamsparta.ecommerce.domain.product.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class EventApplyService(
    private val eventApplyRepository: EventApplyRepository,
    private val productRepository: ProductRepository,
    private val eventRepository: EventRepository
) {

    @Transactional
    fun createEventApply(request: EventApplyRequest): EventApply {
        val product = productRepository.findById(request.productId)
            .orElseThrow { IllegalArgumentException("Product not found with id: ${request.productId}") }
        println("{request.event},{request.productId}")

        if (eventApplyRepository.existsByProductAndEvent(product, request.event)) {
            throw IllegalArgumentException("An event apply already exists for the product with id: ${request.productId} and event: ${request.event}")
        }

        val eventApply = EventApply(
            product = product,
            discountRate = request.discountRate,
            applicationDate = LocalDateTime.now(),
            event = request.event
        )

        return eventApplyRepository.save(eventApply)
    }
    @Transactional
    fun createEvent(apply: EventApply): Event {
        val discountedPrice = calculateDiscountedPrice(apply.product.price, apply.discountRate)
        val event = Event(
            eventApply = apply,
            discountedPrice = discountedPrice
        )
        return event
    }

    @Transactional
    fun save(deal: Event): Event {
        return eventRepository.save(deal)
    }

    private fun calculateDiscountedPrice(originalPrice: Int, discountRate: Int): Int {
        return originalPrice - (originalPrice * discountRate / 100)
    }
}
