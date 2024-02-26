package com.teamsparta.ecommerce.web.response

import lombok.Getter
import lombok.Setter

@Getter
@Setter
data class SingleResponse<T>(
    var message: String? = null,
    var data: T? = null
) {
    companion object {
        fun <T> of(message: String?, data: T?): SingleResponse<T> {
            return SingleResponse(message, data)
        }

        fun <T> successOf(data: T): SingleResponse<T> {
            return of("성공", data)
        }

        fun <T> successOf(message: String, data: T): SingleResponse<T> {
            return of(message, data)
        }

        fun <T> success(): SingleResponse<T> {
            return of("성공", null)
        }

        fun <T> success(message: String): SingleResponse<T> {
            return of( message, null)
        }

        fun <T> failOf(data: T): SingleResponse<T> {
            return of("실패", data)
        }
    }
}