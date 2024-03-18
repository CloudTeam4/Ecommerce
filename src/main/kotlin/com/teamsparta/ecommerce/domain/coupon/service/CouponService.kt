package com.teamsparta.ecommerce.domain.coupon.service

import com.teamsparta.ecommerce.domain.coupon.model.Coupon
import com.teamsparta.ecommerce.domain.coupon.model.CouponBox
import com.teamsparta.ecommerce.domain.coupon.repository.CouponBoxRepository
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
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisOperations
import java.util.concurrent.TimeUnit
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.SessionCallback

@Service
class CouponService(
    private val couponRepository: CouponRepository,
    private val memberRepository: MemberRepository,
    private val redisTemplate: RedisTemplate<String, String>,
    private val rabbitService: RabbitService,
    @Value("\${couponKey}")
    private val COUPON_COUNT_KEY: String,
    private val couponBoxRepository: CouponBoxRepository
    ) {


    private val logger = LoggerFactory.getLogger(CouponService::class.java)

    /**
     * 관리자 선착순 쿠폰 등록
     * */
    @Transactional
    fun addCoupon(memberId: Long, request: CouponCreateRequest): Coupon {
        val member = memberRepository.findById(memberId).orElseThrow { NotFoundException("관리자 정보를 찾을 수 없습니다.") }
        if (member.role != Role.ADMIN) {
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
    fun downloadCoupon(memberId: Long, couponId: Long): String {
        val key = "couponBox:$couponId"
        val member = memberRepository.findById(memberId).orElseThrow { NotFoundException("회원 정보를 찾을 수 없습니다.") }
        val coupon = couponRepository.findById(couponId).orElseThrow { NotFoundException("쿠폰 정보를 찾을 수 없습니다.") }

        val status = redisTemplate.opsForSet().isMember(key, memberId) ?: throw BadRequestException("확인을 못하는중")

        if(!status){
            throw BadRequestException("이미 쿠폰을 발급 받으셨습니다.")
        }

        // Redis 카운터 증가
        val count = redisTemplate.opsForValue().increment(COUPON_COUNT_KEY) ?: 0
        logger.info("현재 쿠폰 수량 : {}", count)

        // 카운터가 정해진 수량을 초과하면 쿠폰 발급 거부
        if(count >= coupon.quantity){
            throw BadRequestException("죄송합니다, 쿠폰이 모두 소진되었습니다.", ErrorCode.BAD_REQUEST)
        }

        redisTemplate.opsForSet().add(key, memberId.toString())
        rabbitService.sendMessage(couponId.toString(), memberId.toString())

        return memberId.toString()
    }



    @Transactional
    fun assignCouponToUsers(role: Role, couponId: Long): List<CouponBox> {
        val members = memberRepository.findByRole(role)
        if (members.isEmpty()) throw NotFoundException("회원 정보를 찾을 수 없습니다.")

        val coupon = couponRepository.findById(couponId).orElseThrow { NotFoundException("쿠폰 정보를 찾을 수 없습니다.") }

        val couponBoxes = mutableListOf<CouponBox>()
        for (member in members) {
            // 해당 멤버가 이미 쿠폰을 받았는지 확인
            val existingCouponBox = couponBoxRepository.findByMemberAndCoupon(member, coupon)
            if (!existingCouponBox.isPresent) {
                // 멤버가 쿠폰을 받지 않았다면 새로운 쿠폰 박스를 생성
                val newCouponBox = CouponBox(member = member, coupon = coupon)
                couponBoxes.add(newCouponBox)
            } else {
                logger.info("멤버 ID ${member.id}는(은) 이미 쿠폰명 ${coupon.name}를 받았습니다.")
            }
        }

        // 새로운 쿠폰 박스들 저장
        return couponBoxRepository.saveAll(couponBoxes)
    }
}
