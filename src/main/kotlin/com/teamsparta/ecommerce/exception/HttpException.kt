package com.teamsparta.ecommerce.exception

// 예외 처리할 클래스(부모클래스)
open class HttpException(
    val statusCode: Int,
    val customMessage: String,
    val error: ErrorCode? = null,
) : RuntimeException()

enum class ErrorCode {
    BAD_REQUEST,
    DUPLICATE_ENTITY,
    NOT_FOUND,
    FORBIDDEN,
    UNAUTHORIZED,
    INTERNAL_SERVER_ERROR,
    JWT_EXPIRED,
    JWT_UNSUPPORTED,
    JWT_WRONG_TYPE,
}