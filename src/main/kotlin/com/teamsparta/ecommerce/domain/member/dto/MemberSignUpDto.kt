package com.teamsparta.ecommerce.domain.member.dto

import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull


data class MemberSignUpDto(

    @field:Email(message = "Invalid Email")
    @field:NotNull(message = "Email is a required field")
    val email: String,


    @field:Size(min = 4, max = 12, message = "Password must be between 4 and 10")
    @field:Pattern(regexp = "^[a-zA-Z0-9!@#\$%^&*]+$", message = "Invalid password")
    @field:NotNull(message = "Password is a required field")
    val password: String,

    @field:NotNull(message = "Phone number is a required field")
    val phone : String,

    @field:NotNull(message = "Nickname is a required field")
    val nickname : String,

    @field:NotNull(message = "Address is a required field")
    val address : String,

    val role : String,
    val adminCode : String
)
