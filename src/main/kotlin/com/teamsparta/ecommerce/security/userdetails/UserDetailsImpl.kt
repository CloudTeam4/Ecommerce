package com.teamsparta.ecommerce.security.userdetails

import com.teamsparta.ecommerce.domain.member.model.Member
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.security.Principal
import javax.security.auth.Subject

class UserDetailsImpl(private val member : Member) : UserDetails, Principal {

    fun getMember() : Member{
        return member
    }
    fun getMemberId() : Long{
        return member.id!!
    }


    override fun getAuthorities(): MutableList<SimpleGrantedAuthority> {
        val authority : MutableList<SimpleGrantedAuthority> = mutableListOf()
        authority.add(SimpleGrantedAuthority(member.role.name))

        return authority

    }

    override fun getName(): String {
        return member.id.toString()
    }

    override fun getUsername(): String {
        return member.email
    }

    override fun getPassword(): String {
        return member.password
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun implies(subject: Subject?): Boolean {
        return super.implies(subject)
    }



}