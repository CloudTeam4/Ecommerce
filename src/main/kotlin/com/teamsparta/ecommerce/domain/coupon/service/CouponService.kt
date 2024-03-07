package com.teamsparta.ecommerce.domain.coupon.service

import com.teamsparta.ecommerce.domain.coupon.model.Coupon
import com.teamsparta.ecommerce.domain.coupon.repository.CouponCountRepository
import com.teamsparta.ecommerce.domain.coupon.repository.CouponRepository
import com.teamsparta.ecommerce.domain.member.repository.MemberRepository
import com.teamsparta.ecommerce.exception.BadRequestException
import com.teamsparta.ecommerce.exception.ErrorCode
import com.teamsparta.ecommerce.exception.NotFoundException
import com.teamsparta.ecommerce.util.enum.Role
import com.teamsparta.ecommerce.util.rabbit.RabbitService
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
    private val couponCountRepository: CouponCountRepository,
    private val couponRepository: CouponRepository,
    private val memberRepository: MemberRepository,
    private val redissonClient: RedissonClient,
    private val redisTemplate: RedisTemplate<String, String>,
    private val rabbitService: RabbitService,

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
            quantity = request.quantity,
            member = member
        )
        return couponRepository.save(coupon)

    }


    /**
     * 사용자 선착순 쿠폰 발급
     * */
    @Transactional
    fun downloadCoupon(memberId: Long, couponId: Long): String {
        val lock = redissonClient.getLock("$couponId") // couponId를 키로 하는 Lock 조회
        val isLocked = lock.tryLock(10, 3, TimeUnit.SECONDS) // 10초 동안 Lock 획득, 이후 3초간 Lock 유지.

        try {
            // Lock 획득 실패
            if (!isLocked) {
                throw RuntimeException("Lock 획득 실패")
            }

            val member = memberRepository.findById(memberId).orElseThrow { NotFoundException("회원 정보를 찾을 수 없습니다.") }
            val coupon = couponRepository.findById(couponId).orElseThrow { NotFoundException("쿠폰 정보를 찾을 수 없습니다.") }

            // 발급 전에 redis의 카운터 확인
            val couponNum = couponCountRepository.getCount()
            logger.info("현재 쿠폰 수량 : {}", couponNum)

            // 카운터가 정해진 수량을 초과하면 쿠폰 발급 거부
            if (couponNum > coupon.quantity) {
                throw BadRequestException("죄송합니다, 쿠폰이 모두 소진되었습니다!!!", ErrorCode.BAD_REQUEST)
            }

            // Redis 카운터 증가
            val count = couponCountRepository.increment()

            // Redis에 쿠폰 정보 저장( memberId, couponId )
            val key = "couponBox:$couponId"
            redisTemplate.opsForValue().set(key, memberId.toString())

            rabbitService.sendMessage(couponId.toString(), memberId.toString())

            return memberId.toString()

        } catch (e: InterruptedException) {
            throw Exception("Thread Interrupted")
        } finally {
            // Lock 반환( Lock의 주체가 이 로직을 호출한 쓰레드일 경우에만 반환 )
            if (lock.isLocked && lock.isHeldByCurrentThread) {
                lock.unlock()
            }
        }
    }
}