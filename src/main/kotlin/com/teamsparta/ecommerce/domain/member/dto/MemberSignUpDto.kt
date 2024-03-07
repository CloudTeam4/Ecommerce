package com.teamsparta.ecommerce.domain.member.dto

import jakarta.annotation.Nullable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull


data class MemberSignUpDto(

    @field:Email(message = "Invalid Email")
    @field:NotNull(message = "Email is a required field")
    val email: String,


    @field:NotNull(message = "Password is a required field")
    val password: String,

    @field:NotNull(message = "Phone number is a required field")
    val phone : String,

    @field:NotNull(message = "Nickname is a required field")
    val nickname : String,

    @field:NotNull(message = "Address is a required field")
    val address : String,

    @field:NotNull
    val role : String,

    @field:Nullable
    val adminCode : String
)
