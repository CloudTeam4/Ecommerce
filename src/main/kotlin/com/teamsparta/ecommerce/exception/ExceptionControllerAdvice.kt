package com.teamsparta.ecommerce.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

// @RestControllerAdvice를 사용하여 모든 @Controller에서 발생하는 예외를 처리
// 각각의 예외에 대해서 @ExceptionHandler로 처리.
// 4xx, 5xx 에러 모두를 하나씩 처리하기에 너무 많기 때문에 부모 클래스 HttpExcpetion을 처리하여 자식 에러를 모두 처리하는 방식으로 구현
@RestControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler
    fun httpExceptionHandler(e: HttpException): ResponseEntity<ExceptionResult> {
        val exceptionResult = ExceptionResult(e.statusCode, e.customMessage, e.error)

        return ResponseEntity(exceptionResult, HttpStatus.valueOf(e.statusCode))
    }

    @ExceptionHandler
    fun handleValidationExceptions(e: MethodArgumentNotValidException): ResponseEntity<ExceptionResult> {
        val errors = e.bindingResult.fieldErrors.joinToString(separator = "; ") { it.defaultMessage ?: "Validation error" }
        val exceptionResult = ExceptionResult(e.statusCode.value(), errors, ErrorCode.BAD_REQUEST)

        return ResponseEntity(exceptionResult, HttpStatus.valueOf(e.statusCode.value()))
    }
}
