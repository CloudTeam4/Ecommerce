package com.teamsparta.ecommerce.domain.member.controller

import com.teamsparta.ecommerce.domain.member.dto.MemberLoginDto
import com.teamsparta.ecommerce.domain.member.dto.MemberSignUpDto
import com.teamsparta.ecommerce.domain.member.service.MemberService
import com.teamsparta.ecommerce.util.web.response.SingleResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/members")
class MemberController(private val memberService: MemberService) {

    @PostMapping
    @RequestMapping("/signup")
    fun memberSignup(@Valid @RequestBody memberSignUpDto: MemberSignUpDto)
            : ResponseEntity<SingleResponse<String>> {
        memberService.memberRegistration(memberSignUpDto)
        return ResponseEntity(SingleResponse.success(), HttpStatus.OK)

    }

    @PostMapping
    @RequestMapping("/login")
    fun memberLogin(@RequestBody memberLoginDto: MemberLoginDto)
            : ResponseEntity<SingleResponse<String>> {

        memberService.memberLogin(memberLoginDto)
        return ResponseEntity(SingleResponse.success(), HttpStatus.OK)
    }
}