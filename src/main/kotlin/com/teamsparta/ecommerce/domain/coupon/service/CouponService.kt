package com.teamsparta.ecommerce.domain.coupon.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.teamsparta.ecommerce.domain.coupon.model.Coupon
import com.teamsparta.ecommerce.domain.coupon.model.CouponBox
import com.teamsparta.ecommerce.domain.coupon.repository.CouponBoxRepository
import com.teamsparta.ecommerce.domain.coupon.repository.CouponCountRepository
import com.teamsparta.ecommerce.domain.coupon.repository.CouponRepository
import com.teamsparta.ecommerce.domain.member.repository.MemberRepository
import com.teamsparta.ecommerce.exception.BadRequestException
import com.teamsparta.ecommerce.exception.ErrorCode
import com.teamsparta.ecommerce.exception.NotFoundException
import com.teamsparta.ecommerce.util.enum.Role
import com.teamsparta.ecommerce.util.web.request.CouponCreateRequest
import jakarta.transaction.Transactional
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Service
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate

@Service
class CouponService(
    private val memberRepository: MemberRepository,
    private val couponRepository: CouponRepository,
    private val couponCountRepository: CouponCountRepository,
    private val couponBoxRepository: CouponBoxRepository,
    private val redissonClient: RedissonClient,
    private val redisTemplate: RedisTemplate<String, String>,

    @Value("\${spring.data.redis.lock.coupon}")
    private val couponLockName: String

) {

    private val logger = LoggerFactory.getLogger(CouponService::class.java)

    /**
     * 관리자 선착순 쿠폰 등록
     * */
    @Transactional
    fun addCoupon(memberId: Long, request: CouponCreateRequest): Coupon {
        val member = memberRepository.findById(memberId).orElseThrow { NotFoundException("관리자 정보를 찾을 수 없습니다.") }
        if(member.role != Role.ADMIN) {
            throw BadRequestException("관리자만 쿠폰을 등록할 수 있습니다.")
        }
        val coupon = Coupon(
            name = request.name,
            explanation = request.explanation,
            deductedPrice = request.deductedPrice,
            status = request.status,
            type = request.type,
            applicable = request.applicable,
            member = member
        )
        return couponRepository.save(coupon)

    }


    /**
     * 사용자 선착순 쿠폰 발급
     * */
    fun downloadCoupon(memberId: Long, couponId: Long): CouponBox {
        val lock = redissonClient.getLock(couponLockName)

        try {
            // 특정 시간동안 락 획득( 최대로 대기할 시간, 최대 대기 시간 동안 대기 중인 스레드들 사이의 간격, 시간의 단위 )
            if (!lock.tryLock(10, 3, TimeUnit.SECONDS)) {
                throw RuntimeException("Lock 획득 실패")
            }

            val member = memberRepository.findById(memberId).orElseThrow { NotFoundException("회원 정보를 찾을 수 없습니다.") }
            val coupon = couponRepository.findById(couponId).orElseThrow { NotFoundException("쿠폰 정보를 찾을 수 없습니다.") }

            // 발급 전에 redis의 카운터 확인
            val couponNum = couponCountRepository.getCount()
            logger.info("현재 쿠폰 수량 : {}", couponNum)

            // 카운터가 정해진 수량을 초과하면 쿠폰 발급 거부
            if (couponNum > 100) {
                throw BadRequestException("죄송합니다, 쿠폰이 모두 소진되었습니다!!!", ErrorCode.BAD_REQUEST)
            }

            // Redis 카운터 증가
            val count = couponCountRepository.increment()

            // Redis에 쿠폰 정보 저장
            // CouponBox 객체를 JSON 형태의 문자열로 변환
            val couponBox = CouponBox(member = member, coupon = coupon)
            val couponBoxJson = ObjectMapper().writeValueAsString(couponBox)
            redisTemplate.opsForValue().set("couponBox:$memberId:$couponId", couponBoxJson)

            return couponBox

        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        } finally {
            // 락이 걸려있다면 락 해제
            if (lock.isLocked) {
                lock.unlock()
            }
        }
    }
}