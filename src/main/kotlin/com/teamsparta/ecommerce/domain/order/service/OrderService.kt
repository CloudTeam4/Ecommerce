package com.teamsparta.ecommerce.domain.order.service

import com.teamsparta.ecommerce.domain.coupon.repository.CouponRepository
import com.teamsparta.ecommerce.domain.event.model.Event
import com.teamsparta.ecommerce.domain.event.repository.EventApplyRepository
import com.teamsparta.ecommerce.domain.event.repository.EventRepository
import com.teamsparta.ecommerce.domain.member.model.Member
import com.teamsparta.ecommerce.domain.member.repository.MemberRepository
import com.teamsparta.ecommerce.domain.order.dto.CallOrderListDto
import com.teamsparta.ecommerce.domain.order.dto.OrderItemRequestDto
import com.teamsparta.ecommerce.domain.order.dto.OrderRequestDto
import com.teamsparta.ecommerce.domain.order.dto.OrderResponseDto
import com.teamsparta.ecommerce.domain.order.model.Order
import com.teamsparta.ecommerce.domain.order.model.OrderDetail
import com.teamsparta.ecommerce.domain.order.repository.OrderRepository
import com.teamsparta.ecommerce.domain.product.repository.ProductRepository
import com.teamsparta.ecommerce.util.enum.CouponStatus
import com.teamsparta.ecommerce.util.enum.Status
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
    fun createOrder(orderRequestDto: OrderRequestDto, memberEmail: String): OrderResponseDto {
        val member = memberRepository.findByEmail(memberEmail).orElseThrow { RuntimeException("회원을 찾을 수 없습니다.") }
        val order = initializeOrder(member, orderRequestDto)

        val totalOrderPrice = calculateTotalOrderPrice(order, orderRequestDto)
        applyCouponIfAvailable(orderRequestDto.couponId, totalOrderPrice)

        order.totalprice = totalOrderPrice
        orderRepository.save(order)

        return OrderResponseDto(orderdate = order.orderdate, paymentmethod = order.paymentmethod, totalprice = order.totalprice)
    }

    fun initializeOrder(member: Member, orderRequestDto: OrderRequestDto): Order {
        return Order(member = member, orderdate = LocalDateTime.now(), paymentmethod = orderRequestDto.paymentmethod)
    }

    fun calculateTotalOrderPrice(order: Order, orderRequestDto: OrderRequestDto): Int {
        var totalOrderPrice = 0
        orderRequestDto.items.forEach { itemDto ->
            totalOrderPrice += processOrderItem(itemDto, order)
        }
        return totalOrderPrice
    }


    fun processOrderItem(itemDto: OrderItemRequestDto, order: Order): Int {
        val product = productRepository.findById(itemDto.productId).orElseThrow { RuntimeException("상품을 찾을 수 없습니다.") }
        val event = findBestEventByProductId(product.itemId!!)
        val orderDetail = OrderDetail(order = order, product = product, quantity = itemDto.quantity)
        order.orderDetails.add(orderDetail)
        return calculatePrice(product.price, itemDto.quantity, event?.discountedPrice)
    }

    fun applyCouponIfAvailable(couponId: Long?, totalOrderPrice: Int): Int {
        return couponId?.let {
            val coupon = couponRepository.findById(it).orElseThrow { RuntimeException("유효하지 않은 쿠폰입니다.") }
            if (coupon.isValid()) {
                coupon.couponstatus = CouponStatus.USED
                couponRepository.save(coupon)
                totalOrderPrice - coupon.deductedPrice // 새로운 totalOrderPrice 값을 반환
            } else {
                throw RuntimeException("쿠폰 사용 조건을 충족하지 않습니다.")
            }
        } ?: totalOrderPrice // 쿠폰 ID가 null인 경우, 원래의 totalOrderPrice 반환
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
    fun deleteOrder(orderId: Long,memberEmail: String) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if ((order.member.email == memberEmail)&&((order.status == Status.ORDERCANCEL) || (order.status == Status.DELIVERYCOMPLETED)))
            orderRepository.deleteById(orderId)
    }

    @Transactional
    fun cancelOrder(orderId: Long,memberEmail: String) {
        val order = orderRepository.findById(orderId).orElseThrow()
        if ((order.member.email == memberEmail)&&(order.status == Status.PAYMENTCOMPLETED||order.status == Status.PRIPAIRINGPRODUCT))
            order.status = Status.ORDERCANCEL
    }
    @Transactional
    fun refundOrder(orderId: Long,memberEmail: String) {
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
