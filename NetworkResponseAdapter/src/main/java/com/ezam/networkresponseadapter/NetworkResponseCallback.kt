package com.ezam.networkresponseadapter

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException
import kotlin.reflect.KClass



class NetworkResponseCallback<S, E : Any>(
    private val networkResponseCall: NetworkResponseCall<S, E>,
    private val callback: Callback<NetworkResponse<S, E>>,
    private val errorConverter: Converter<ResponseBody, E>
) : Callback<S> {
    /**
     * Invoked for a received HTTP response.
     *
     *
     * Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
     * Call [Response.isSuccessful] to determine if the response indicates success.
     */
    override fun onResponse(call: Call<S>, response: Response<S>) {
        val body = response.body()
        val code = response.code()
        val error = response.errorBody()

        if( response.isSuccessful ){

            callback.onResponse(
                networkResponseCall,
                Response.success( NetworkResponse.Success(body as S) )
            )
        } else {

            val errorBody : E? = when {
                error == null -> null
                error.contentLength() == 0L -> null
                else -> try {
                    errorConverter.convert( error )
                } catch ( e: Exception){
                    null
                }
            }

            if(errorBody != null){
                callback.onResponse(
                    networkResponseCall,
                    Response.success( NetworkResponse.ApiError(errorBody,code))
                )
            } else {
                callback.onResponse(
                    networkResponseCall,
                    Response.success( NetworkResponse.UnknownError(null) )
                )
            }
        }
    }

    inline fun <reified T> conv() {

    }

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected exception
     * occurred creating the request or processing the response.
     */
    override fun onFailure(call: Call<S>, t: Throwable) {
        val networkResponse = when( t ) {
            is IOException -> NetworkResponse.NetworkError(t)
            else -> NetworkResponse.UnknownError( t )
        }
        callback.onResponse(
            networkResponseCall,
            Response.success( networkResponse )
        )
    }

}