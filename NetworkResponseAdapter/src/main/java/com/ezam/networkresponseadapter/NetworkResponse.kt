package com.ezam.networkresponseadapter

import java.io.IOException

/**
 * A network response
 *
 * This class represents the possible scenarios of a network request
 *
 * @param T successful response type
 * @param U error response type
 */
sealed class NetworkResponse<out T, out U: Any>{
    /**
     * Success response with [body] of type [T]
     */
    data class Success<T>(val body: T) : NetworkResponse<T, Nothing>()

    /**
     * Failure response with [body] of type [U]
     */
    data class ApiError<U : Any>(val body: U, val code: Int) : NetworkResponse<Nothing, U>()

    /**
     * Network error
     */
    data class NetworkError(val error: IOException): NetworkResponse<Nothing, Nothing>()

    /**
     * Any other error like parsing error
     */
    data class UnknownError(val error: Throwable?): NetworkResponse<Nothing, Nothing>()
}
