package com.ezam.networkresponseadapter

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

class NetworkResponseAdapter<S, E: Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, E>
) : CallAdapter<S, Call<NetworkResponse<S, E>>>{
    /**
     * Returns the value type that this adapter uses when converting the HTTP response body to a Java
     * object. For example, the response type for `Call<Repo>` is `Repo`. This type is
     * used to prepare the `call` passed to `#adapt`.
     *
     *
     * Note: This is typically not the same type as the `returnType` provided to this call
     * adapter's factory.
     */
    override fun responseType(): Type = successType

    /**
     * Returns an instance of `T` which delegates to `call`.
     *
     *
     * For example, given an instance for a hypothetical utility, `Async`, this instance
     * would return a new `Async<R>` which invoked `call` when run.
     *
     * <pre>`
     * &#64;Override
     * public <R> Async<R> adapt(final Call<R> call) {
     * return Async.create(new Callable<Response<R>>() {
     * &#64;Override
     * public Response<R> call() throws Exception {
     * return call.execute();
     * }
     * });
     * }
    `</pre> *
     */
    override fun adapt(call: Call<S>): Call<NetworkResponse<S, E>> {
        return NetworkResponseCall( call, errorBodyConverter)
    }
}