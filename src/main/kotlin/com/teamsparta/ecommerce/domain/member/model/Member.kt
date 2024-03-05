package com.teamsparta.ecommerce.domain.member.model

import com.teamsparta.ecommerce.util.enum.Role
import jakarta.persistence.*


@Entity
data class Member(

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var phone : String,

    @Column(nullable = false)
    var address : String,

    @Column(nullable = false)
    var nickname : String,

    @Enumerated(EnumType.STRING)
    var role : Role

){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

}
