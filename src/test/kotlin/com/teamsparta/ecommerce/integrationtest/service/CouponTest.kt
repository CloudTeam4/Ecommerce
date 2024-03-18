import com.teamsparta.ecommerce.domain.coupon.repository.CouponBoxRepository
import com.teamsparta.ecommerce.domain.coupon.repository.CouponRepository
import com.teamsparta.ecommerce.domain.coupon.service.CouponService
import com.teamsparta.ecommerce.domain.member.dto.MemberSignUpDto
import com.teamsparta.ecommerce.domain.member.repository.MemberRepository
import com.teamsparta.ecommerce.exception.NotFoundException
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
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors



class CouponTest : IntegrationTest() {



    @Test
//    @Transactional
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

        val coupon = couponRepository.findById(1).orElseThrow {
            NotFoundException("nono")
        }


        assertThat(coupon.name == "testCoupon")


    }

    @Test
    @Transactional
    fun issueCouponByFirstComeTest(){

        val coupon = couponRepository.findById(1).orElseThrow {
            NotFoundException("11")
        }
        val threadCount = coupon.quantity+100
        val executorService = Executors.newFixedThreadPool(200)
        val countDownLatch = CountDownLatch(threadCount)

        for (i in 0 until threadCount) {
            val dto = MemberSignUpDto(
                email = "test${i}@example.com",
                password = "test${i}",
                phone = "0000000000",
                nickname = "test${i}",
                address = "testAdmin${i}",
                role = "CUSTOMER",
                adminCode = ""
            )
            memberService.memberRegistration(dto)
        }

        for(i in 0 until threadCount){
            executorService.submit{
                try {
                    couponService.downloadCoupon(i.toLong(),1L)

                }finally {
                    countDownLatch.countDown()
                }
            }
        }

        assertThat(redisTemplate.opsForSet().size("couponBox:1") == coupon.quantity.toLong())


    }
}
