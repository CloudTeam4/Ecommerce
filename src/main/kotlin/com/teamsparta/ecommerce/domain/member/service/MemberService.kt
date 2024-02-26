package com.teamsparta.ecommerce.domain.member.service

import com.teamsparta.ecommerce.domain.member.dto.MemberLoginDto
import com.teamsparta.ecommerce.domain.member.dto.MemberSignUpDto
import com.teamsparta.ecommerce.domain.member.model.Member
import com.teamsparta.ecommerce.domain.member.repository.MemberRepository
import com.teamsparta.ecommerce.exception.*
import com.teamsparta.ecommerce.util.enum.Role
import com.teamsparta.ecommerce.util.jwt.JwtUtil
import jakarta.servlet.http.HttpServletResponse
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val jwtUtil: JwtUtil,
    private val response: HttpServletResponse,
    @Value("\${secretAdminCode}")
    private val secretCode: String


) {

    @Transactional
    fun memberRegistration(memberSignUpDto: MemberSignUpDto) {
        val email = memberSignUpDto.email
        val encodedPassword = passwordEncoder.encode(memberSignUpDto.password)

        if (memberRepository.existsByEmail(email)) {
            throw ConflictException("Already used id")
        }

        checkRoleAndSaveMember(memberSignUpDto, encodedPassword)

    }

    @Transactional
    fun memberLogin(memberLoginDto: MemberLoginDto): String {

        val email = memberLoginDto.email
        val password = memberLoginDto.password

        if (!memberRepository.existsByEmail(email)) {
            throw ForbiddenException("Id or password is wrong. Try again")
        }

        val requestMember = memberRepository.findByEmail(email).orElseThrow {
            throw ForbiddenException("Id or password is wrong. Try again")
        }

        if (!passwordEncoder.matches(password, requestMember.password)) {
            throw ForbiddenException("Id or password is wrong. Try again")
        }

        val generalToken = jwtUtil.generateGeneralToken(requestMember.id!!, email, requestMember.role)

        response.addHeader("Authorization", "Bearer $generalToken")

        return "Login success"

    }

    private fun checkRoleAndSaveMember(memberSignUpDto: MemberSignUpDto, encodedPassword: String) {
        if (memberSignUpDto.role == "CUSTOMER") {
            memberRepository.save(
                Member(
                    email = memberSignUpDto.email,
                    password = encodedPassword,
                    phone = memberSignUpDto.phone,
                    address = memberSignUpDto.address,
                    nickname = memberSignUpDto.nickname,
                    role = Role.CUSTOMER
                )
            )
        } else if (memberSignUpDto.role == "SELLER") {
            memberRepository.save(
                Member(
                    email = memberSignUpDto.email,
                    password = encodedPassword,
                    phone = memberSignUpDto.phone,
                    address = memberSignUpDto.address,
                    nickname = memberSignUpDto.nickname,
                    role = Role.SELLER
                )
            )
        } else if (memberSignUpDto.role == "ADMIN") {
            if (memberSignUpDto.adminCode == secretCode) {
                memberRepository.save(
                    Member(
                        email = memberSignUpDto.email,
                        password = encodedPassword,
                        phone = memberSignUpDto.phone,
                        address = memberSignUpDto.address,
                        nickname = memberSignUpDto.nickname,
                        role = Role.ADMIN
                    )
                )
            } else {
                throw ForbiddenException("Check your admin code again")
            }

        }

    }
}