package com.teamsparta.ecommerce.domain.member.dto

import jakarta.annotation.Nullable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull


data class MemberSignUpDto(

    @field:Email(message = "Invalid Email")
    @field:NotBlank(message = "Email is a required field")
    val email: String,


    @field:NotBlank(message = "Password is a required field")
    val password: String,

    @field:NotBlank(message = "Phone number is a required field")
    val phone : String,

    @field:NotBlank(message = "Nickname is a required field")
    val nickname : String,

    @field:NotBlank(message = "Address is a required field")
    val address : String,

    @field:NotNull
    val role : String,

    @field:Nullable
    val adminCode : String
)
