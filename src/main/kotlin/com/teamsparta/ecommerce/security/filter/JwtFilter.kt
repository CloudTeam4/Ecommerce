package com.teamsparta.ecommerce.security.filter

import com.teamsparta.ecommerce.util.jwt.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter (private val jwtUtil: JwtUtil) : OncePerRequestFilter(){

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain){

        val bearerToken = request.getHeader("Authorization")
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            val jwtToken = bearerToken.substring(7)
            if (jwtUtil.validateToken(jwtToken)) {
                val memberId = jwtUtil.getMemberIdFromToken(jwtToken) ?: throw NullPointerException("The username is null.")

                val authentication = jwtUtil.createAuthentication(memberId)

                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        filterChain.doFilter(request, response)

    }

}