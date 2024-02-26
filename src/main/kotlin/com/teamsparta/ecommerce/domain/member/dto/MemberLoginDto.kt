package com.teamsparta.ecommerce.domain.member.dto

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Email


data class MemberLoginDto(
    @field:Email(message = "Invalid Email")
    val email : String,


    @field:Size(min = 4, max = 12, message = "Password must be between 4 and 10")
    @field:Pattern(regexp = "^[a-zA-Z0-9!@#\$%^&*]+$", message = "Invalid password")
    val password: String
)
