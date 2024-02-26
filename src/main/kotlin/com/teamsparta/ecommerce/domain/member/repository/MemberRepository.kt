package com.teamsparta.ecommerce.domain.member.repository

import com.teamsparta.ecommerce.domain.member.model.Member
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MemberRepository : JpaRepository<Member, UUID> {

    fun existsByEmail (memberName : String) : Boolean

    fun findByEmail (memberName: String) : Optional<Member>


}