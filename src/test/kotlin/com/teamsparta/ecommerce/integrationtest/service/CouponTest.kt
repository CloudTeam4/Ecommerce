import com.teamsparta.ecommerce.domain.coupon.repository.CouponBoxRepository
import com.teamsparta.ecommerce.domain.coupon.repository.CouponRepository
import com.teamsparta.ecommerce.domain.coupon.service.CouponService
import com.teamsparta.ecommerce.domain.member.repository.MemberRepository
import com.teamsparta.ecommerce.integrationtest.IntegrationTest
import com.teamsparta.ecommerce.util.enum.Role
import com.teamsparta.ecommerce.util.rabbit.RabbitService
import com.teamsparta.ecommerce.util.web.request.CouponCreateRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.redis.core.RedisTemplate



class CouponTest : IntegrationTest() {



    @Test
    fun issueCouponTest() {
        val couponCreateRequestDto = CouponCreateRequest(
            name = "testCoupon",
            explanation = "테스트용입니다",
            deductedPrice = 1000,
            status = "Not Expired",
            type = false,
            applicable = Role.CUSTOMER.toString(),
            quantity = 100
        )
        couponService.addCoupon(1, couponCreateRequestDto)

        assertThat(couponRepository.existsById(2))


    }

//    @Test
//    fun issueCouponByFirstComeTest(){
//        val couponId = 1L
//        val threadCount = 1000
//        val executorService = Executors.newFixedThreadPool(32)
//        val countDownLatch = CountDownLatch(threadCount)
//
//        for (i in 0 until threadCount) {
//            val memberId = i.toLong()
//            executorService.submit {
//                try {
//                    couponService.downloadCoupon(memberId, couponId)
//                } finally {
//                    countDownLatch.countDown()
//                }
//            }
//        }
//
//
//    }
}
