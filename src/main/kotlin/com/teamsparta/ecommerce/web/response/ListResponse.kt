package com.teamsparta.ecommerce.web.response

import lombok.Getter
import lombok.Setter

@Getter
@Setter
data class ListResponse<T>(
    var message: String? = null,
    var data: List<T>? = null
) {
    companion object {
        fun <T> of(message: String?, data: List<T>?): ListResponse<T> {
            return ListResponse(message, data)
        }

        fun <T> of(message: String?): ListResponse<T> {
            return of(message, null)
        }

        fun <T> successOf(data: List<T>): ListResponse<T> {
            return of("성공", data)
        }

        fun <T> success(): ListResponse<T> {
            return of("성공", null)
        }

        fun <T> failOf(data: List<T>): ListResponse<T> {
            return of("실패", data)
        }
    }
}