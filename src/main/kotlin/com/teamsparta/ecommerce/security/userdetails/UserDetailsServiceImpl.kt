package com.teamsparta.ecommerce.security.userdetails

import com.teamsparta.ecommerce.domain.member.repository.MemberRepository
import com.teamsparta.ecommerce.exception.NotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserDetailsServiceImpl(private val memberRepository: MemberRepository) : UserDetailsService {
    override fun loadUserByUsername(memberId: String?): UserDetails {
        val member = memberRepository
            .findById(
                memberId?.toLong() ?:throw NotFoundException("There is no Member that is related to the memberName"))
            .orElseThrow { throw NotFoundException("There is no Member that is related to the memberName") }

        return UserDetailsImpl(member)


    }
}