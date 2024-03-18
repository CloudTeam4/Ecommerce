package com.teamsparta.ecommerce.domain.order.service

import com.teamsparta.ecommerce.domain.coupon.repository.CouponRepository
import com.teamsparta.ecommerce.domain.event.model.Event
import com.teamsparta.ecommerce.domain.event.repository.EventApplyRepository
import com.teamsparta.ecommerce.domain.event.repository.EventRepository
import com.teamsparta.ecommerce.domain.member.repository.MemberRepository
import com.teamsparta.ecommerce.domain.order.dto.CallOrderListDto
import com.teamsparta.ecommerce.domain.order.dto.OrderRequestDto
import com.teamsparta.ecommerce.domain.order.dto.OrderResponseDto
import com.teamsparta.ecommerce.domain.order.model.Order
import com.teamsparta.ecommerce.domain.order.model.OrderDetail
import com.teamsparta.ecommerce.domain.order.repository.OrderRepository
import com.teamsparta.ecommerce.domain.product.repository.ProductRepository
import com.teamsparta.ecommerce.util.enum.CouponStatus
import com.teamsparta.ecommerce.util.enum.Status
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val memberRepository: MemberRepository,
    private val productRepository: ProductRepository,
    private val couponRepository: CouponRepository,
    private val eventRepository: EventRepository,
    private val eventApplyRepository: EventApplyRepository
) {

    @Transactional
    fun createOrder(orderRequestDto: OrderRequestDto): OrderResponseDto {
        val authentication = SecurityContextHolder.getContext().authentication
        val email = if (authentication != null && authentication.principal is UserDetails) {
            (authentication.principal as UserDetails).username
        } else {
            throw RuntimeException("인증된 사용자 정보를 찾을 수 없습니다.")
        }
        val member = memberRepository.findByEmail(email).orElseThrow { RuntimeException("회원을 찾을 수 없습니다.") }
        val order = Order(member = member, orderdate = LocalDateTime.now(), paymentmethod = orderRequestDto.paymentmethod)
        var totalOrderPrice = 0

        orderRequestDto.items.forEach { itemDto ->
            val product = productRepository.findById(itemDto.productId).orElseThrow { RuntimeException("상품을 찾을 수 없습니다.") }
            val event = findBestEventByProductId(product.itemId!!)
            val orderDetail = OrderDetail(order = order, product = product, quantity = itemDto.quantity)
            totalOrderPrice += calculatePrice(product.price, itemDto.quantity, event?.discountedPrice)

            order.orderDetails.add(orderDetail)
        }

        orderRequestDto.couponId?.let { couponId ->
        val coupon = couponRepository.findById(couponId)
            .orElseThrow { RuntimeException("유효하지 않은 쿠폰입니다.") }

            // 쿠폰 유효성 검사 로직 (사용 기간, 최소 주문 금액, 등)
            if (coupon.isValid()) {
                totalOrderPrice -= coupon.deductedPrice
                coupon.couponstatus = CouponStatus.USED
                couponRepository.save(coupon)
            } else {
                throw RuntimeException("쿠폰 사용 조건을 충족하지 않습니다.")
            }
        }

        order.totalprice = totalOrderPrice
        orderRepository.save(order)

        return OrderResponseDto(orderdate = order.orderdate, paymentmethod = order.paymentmethod, totalprice = order.totalprice)
        }





    fun calculatePrice(productPrice: Int, quantity: Int, discountedPrice: Int?): Int {
        val effectivePrice = discountedPrice ?: productPrice
        return effectivePrice * quantity
    }
    fun findBestEventByProductId(productId: Long): Event? {
        val eventApplies = eventApplyRepository.findByProductItemId(productId)?: emptyList()
        val bestEventApply = eventApplies.maxByOrNull { it.discountRate } // 할인율이 가장 높은 이벤트 적용
        return bestEventApply?.let {
            eventRepository.findById(it.id!!).orElse(null)
        }

    }
    @Transactional
    fun deleteOrder(orderId: Long) {
        val authentication = SecurityContextHolder.getContext().authentication
        val memberEmail = authentication.name
        val order = orderRepository.findById(orderId).orElseThrow()
        if ((order.member.email == memberEmail)&&((order.status == Status.ORDERCANCEL) || (order.status == Status.DELIVERYCOMPLETED)))
            orderRepository.deleteById(orderId)
    }

    @Transactional
    fun cancelOrder(orderId: Long) {
        val authentication = SecurityContextHolder.getContext().authentication
        val memberEmail = authentication.name
        val order = orderRepository.findById(orderId).orElseThrow()
        if ((order.member.email == memberEmail)&&(order.status == Status.PAYMENTCOMPLETED||order.status == Status.PRIPAIRINGPRODUCT))
            order.status = Status.ORDERCANCEL
    }
    @Transactional
    fun refundOrder(orderId: Long) {
        val authentication = SecurityContextHolder.getContext().authentication
        val memberEmail = authentication.name
        val order = orderRepository.findById(orderId).orElseThrow()
        val now = LocalDateTime.now()
        val duration = Duration.between(order.orderdate, now)
        val hoursDifference = duration.toHours()
        if(order.member.email == memberEmail){
            if (hoursDifference <= 168) { // 168시간(7일) 이내
                order.status = Status.ORDERREFUND
            } else {
                throw IllegalStateException("7일 이내에만 반품 할 수 있습니다.")
            }
        }
    }

    @Transactional
    fun orderPreparingProduct(orderId: Long) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if (order.status == Status.PAYMENTCOMPLETED)
            order.status = Status.PRIPAIRINGPRODUCT
    }
    @Transactional
    fun orderDeliveryStart(orderId: Long) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if (order.status == Status.PRIPAIRINGPRODUCT)
            order.status = Status.DELIVERYSTARTED
    }

    @Transactional
    fun orderOnDelivery(orderId: Long) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if (order.status == Status.DELIVERYSTARTED)
            order.status = Status.ONDELIVERY
    }

    @Transactional
    fun orderDeliveryCompleted(orderId: Long) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if (order.status == Status.ONDELIVERY)
            order.status = Status.DELIVERYCOMPLETED
            order.arrivaldate = LocalDateTime.now()
    }

    @Transactional
    fun findOrderList(memberId: Long): List<CallOrderListDto> {
        val excludedStatuses = listOf(Status.ORDERCANCEL, Status.ORDERREFUND)
        val orders = orderRepository.findByMemberIdAndStatusNotIn(memberId, excludedStatuses)
        return orders.map { order ->
            CallOrderListDto(
                orderdate = order.orderdate,
                arrivaldate = order.arrivaldate,
                paymentmethod = order.paymentmethod,
                totalprice = order.totalprice
            )
        }
    }

    @Transactional
    fun findOrderCancelAndRefundList(memberId: Long): List<CallOrderListDto> {
        val statuses = listOf(Status.ORDERCANCEL, Status.ORDERREFUND)
        val orders = orderRepository.findByMemberIdAndStatusIn(memberId, statuses)
        return orders.map { order ->
            CallOrderListDto(
                orderdate = order.orderdate,
                arrivaldate = order.arrivaldate,
                paymentmethod = order.paymentmethod,
                totalprice = order.totalprice
            )
        }
    }

}
