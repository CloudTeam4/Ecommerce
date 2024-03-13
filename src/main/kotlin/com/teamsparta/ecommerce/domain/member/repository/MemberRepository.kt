package com.teamsparta.ecommerce.domain.member.repository

import com.teamsparta.ecommerce.domain.member.model.Member
import com.teamsparta.ecommerce.util.enum.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MemberRepository : JpaRepository<Member, Long> {

    fun findByRole(role: Role): List<Member>

    fun existsByEmail (email : String) : Boolean

    fun findByEmail (email: String) : Optional<Member>




}