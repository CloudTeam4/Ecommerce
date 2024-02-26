package com.teamsparta.ecommerce.exception

// 4xx 에러
class BadRequestException(message: String, error: ErrorCode? = null) : HttpException(400, message, error)
class UnauthorizedException(message: String, error: ErrorCode? = null) : HttpException(401, message, error)
class ForbiddenException(message: String, error: ErrorCode? = null) : HttpException(403, message, error)
class NotFoundException(message: String, error: ErrorCode? = null) : HttpException(404, message, error)
class MethodNotAllowedException(message: String, error: ErrorCode? = null) : HttpException(405, message, error)
class RequestTimeoutException(message: String, error: ErrorCode? = null) : HttpException(408, message, error)
class ConflictException(message: String, error: ErrorCode? = null) : HttpException(409, message, error)

// 5xx 에러
class InternalServerErrorException(message: String, error: ErrorCode? = null) : HttpException(500, message, error)
