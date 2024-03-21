package com.teamsparta.ecommerce.unittest.order


import com.teamsparta.ecommerce.domain.member.model.Member
import com.teamsparta.ecommerce.domain.order.dto.OrderItemRequestDto
import com.teamsparta.ecommerce.domain.order.dto.OrderRequestDto
import com.teamsparta.ecommerce.domain.order.model.Order
import com.teamsparta.ecommerce.domain.order.repository.OrderRepository
import com.teamsparta.ecommerce.domain.order.service.OrderService
import com.teamsparta.ecommerce.domain.product.repository.ProductRepository
import com.teamsparta.ecommerce.util.enum.PaymentMethod
import com.teamsparta.ecommerce.util.enum.Role
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
//import org.mockito.kotlin.any
//import org.mockito.kotlin.anyOrNull
//import org.mockito.kotlin.whenever

@SpringBootTest
@AutoConfigureMockMvc
class OrderUnitTest {
    @MockBean
    private val productRepository: ProductRepository? = null

    @MockBean
    private val orderRepository: OrderRepository? = null

    @Autowired
    private val orderService: OrderService? = null

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun calculateTotalOrderPriceTest() {
        // 상품과 주문 요청 객체를 준비합니다.
        val orderRequestDto = OrderRequestDto(
            paymentmethod = PaymentMethod.CARD,
            totalprice = 500,
            items = listOf(
                OrderItemRequestDto(1L, 2), // 상품 ID 1, 수량 2
                OrderItemRequestDto(2L, 1)  // 상품 ID 2, 수량 1
            )
        )
//
//        whenever(orderService?.processOrderItem(anyOrNull(), anyOrNull())).thenReturn(200).thenReturn(100)
////        whenever(orderService?.processOrderItem(any(), any())).thenReturn(200).thenReturn(100)
//        whenever(orderService?.processOrderItem(Mockito.eq(OrderItemRequestDto(...)), Mockito.any())).thenReturn(200).thenReturn(100)
        whenever(orderService?.processOrderItem(any(), anyOrNull())).thenReturn(200).thenReturn(100)
//
//
//        // processOrderItem 메서드가 각 항목의 가격을 반환하도록 모킹합니다.
//        Mockito.`when`(
//            orderService!!.processOrderItem(
//                any(OrderItemRequestDto::class.java),
//                any(Order::class.java)
//            )
//        )
//            .thenReturn(200) // 첫 번째 항목 가격
//            .thenReturn(100) // 두 번째 항목 가격

        val member: Member = Member(
            email = "baobab523@gmail.com",
            nickname = "dtd",
            password = "486",
            phone = "anj",
            role = Role.ADMIN
        ) // 멤버 객체를 적절히 생성 또는 모킹해야 합니다.
        val order: Order? = orderService?.initializeOrder(member, orderRequestDto)

        // 총합 계산을 실행합니다.
        val totalOrderPrice = orderService?.calculateTotalOrderPrice(order!!, orderRequestDto)

        // 검증: 총 가격이 예상대로 300인지 확인합니다.
        Assertions.assertEquals(300, totalOrderPrice)
    }
}