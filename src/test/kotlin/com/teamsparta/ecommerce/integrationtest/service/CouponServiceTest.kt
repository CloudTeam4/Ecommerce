import com.teamsparta.ecommerce.domain.coupon.repository.CouponBoxRepository
import com.teamsparta.ecommerce.domain.coupon.repository.CouponCountRepository
import com.teamsparta.ecommerce.domain.coupon.repository.CouponRepository
import com.teamsparta.ecommerce.domain.coupon.service.CouponService
import com.teamsparta.ecommerce.domain.member.repository.MemberRepository
import com.teamsparta.ecommerce.integrationtest.IntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.web.WebAppConfiguration


@WebAppConfiguration
@SpringBootTest(classes = [CouponService::class])
class CouponServiceTest : IntegrationTest() {

    @Autowired
    lateinit var couponService: CouponService
    @Autowired
    lateinit var couponCountRepository: CouponCountRepository

    @Test
    fun `여러_사용자_선착순_쿠폰_발급`() {
        val couponId = 1L
        val threadCount = 1000
        val executorService = Executors.newFixedThreadPool(32)
        val countDownLatch = CountDownLatch(threadCount)

        for (i in 1 until threadCount) {
            val memberId = i.toLong()
            executorService.submit {
                try {
                    couponService.downloadCoupon(memberId, couponId)
                } finally {
                    countDownLatch.countDown()
                }
            }
        }

        countDownLatch.await()
        val count = couponCountRepository.getCount()
        assertEquals(100, count)
    }
}
