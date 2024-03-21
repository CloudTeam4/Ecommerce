package com.teamsparta.ecommerce.domain.coupon.controller

import com.teamsparta.ecommerce.domain.coupon.dto.CouponDto
import com.teamsparta.ecommerce.domain.coupon.service.CouponService
import com.teamsparta.ecommerce.exception.BadRequestException
import com.teamsparta.ecommerce.exception.ErrorCode
import com.teamsparta.ecommerce.security.userdetails.UserDetailsImpl
import com.teamsparta.ecommerce.util.web.request.CouponCreateRequest
import com.teamsparta.ecommerce.util.web.response.SingleResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class CouponController(
    private val couponService: CouponService
) {

    /**
     * 관리자 선착순 쿠폰 등록
     * */
    @PostMapping("/api/coupons")
    fun createCoupon(
        @AuthenticationPrincipal user : UserDetailsImpl,
        @Valid @RequestBody request: CouponCreateRequest
    ) : ResponseEntity<SingleResponse<CouponDto>> {
        try {
            val coupon = couponService.addCoupon(user.getMemberId(), request)
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(SingleResponse.successOf("쿠폰 등록에 성공했습니다!", CouponDto.fromEntity(coupon)))
        } catch (e: BadRequestException) {
            throw BadRequestException("쿠폰 등록에 실패했습니다.", ErrorCode.BAD_REQUEST)
        }
    }

    /**
     * 사용자 선착순 쿠폰 발급
     * */
    @PostMapping("/api/coupons/{couponId}")
    fun downloadCoupon(
        @AuthenticationPrincipal user: UserDetailsImpl,
        @PathVariable couponId: Long,
    ) : ResponseEntity<SingleResponse<String>> {
        try {
            return ResponseEntity
                .status(HttpStatus.OK)
                .body(SingleResponse.success("축하합니다! 쿠폰이 발급 되었습니다."))
        } catch (e: BadRequestException) {
            throw BadRequestException("죄송합니다, 쿠폰이 모두 소진되었습니다.", ErrorCode.BAD_REQUEST)
        }
    }
}



