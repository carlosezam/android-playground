package com.ezam.networkresponseadapter

import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response


class NetworkResponseCall<S, E : Any>(
    private val mCall: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<NetworkResponse<S,E>> {
    /**
     * Create a new, identical call to this one which can be enqueued or executed even if this call
     * has already been.
     */
    override fun clone(): Call<NetworkResponse<S, E>> = NetworkResponseCall( mCall, errorConverter)

    /**
     * Synchronously send the request and return its response.
     *
     * @throws IOException if a problem occurred talking to the server.
     * @throws RuntimeException (and subclasses) if an unexpected error occurs creating the request or
     * decoding the response.
     */
    override fun execute(): Response<NetworkResponse<S, E>> {
        throw UnsupportedOperationException("NetworkResponseCall doesn't support excute")
    }

    /**
     * Asynchronously send the request and notify `callback` of its response or if an error
     * occurred talking to the server, creating the request, or processing the response.
     */
    override fun enqueue(callback: Callback<NetworkResponse<S, E>>) = mCall.enqueue( NetworkResponseCallback<S,E>( this, callback, errorConverter ) )

    /**
     * Returns true if this call has been either [executed][.execute] or [ ][.enqueue]. It is an error to execute or enqueue a call more than once.
     */
    override fun isExecuted(): Boolean = mCall.isExecuted

    /**
     * Cancel this call. An attempt will be made to cancel in-flight calls, and if the call has not
     * yet been executed it never will be.
     */
    override fun cancel() = mCall.cancel()

    /** True if [.cancel] was called.  */
    override fun isCanceled(): Boolean = mCall.isCanceled

    /** The original HTTP request.  */
    override fun request(): Request = mCall.request()

    /**
     * Returns a timeout that spans the entire call: resolving DNS, connecting, writing the request
     * body, server processing, and reading the response body. If the call requires redirects or
     * retries all must complete within one timeout period.
     */
    override fun timeout(): Timeout = mCall.timeout()
}