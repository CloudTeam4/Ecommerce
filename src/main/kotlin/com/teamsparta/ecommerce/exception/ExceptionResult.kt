package com.teamsparta.ecommerce.exception

// 예외 발생시 반환할 클래스
data class ExceptionResult(
    val statusCode: Int,
    val message: String,
    val error: ErrorCode? = null,
)