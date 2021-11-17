package com.ezam.networkresponseadapter

import com.google.gson.reflect.TypeToken
import net.bytebuddy.description.method.MethodDescription
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Method
import java.lang.reflect.Type
import kotlin.reflect.KCallable
import kotlin.reflect.KClass

class NetworkResponseAdapterFactoryTest {

    val factory = NetworkResponseAdapterFactory()

    @Test
    fun whenRawTypeIsNotCall_shouldReturnNull(){
        val type: Type = Any::class.java
        val adapter = factory.get( type, emptyArray(), mock() )
        Assert.assertNull( adapter )
    }

    @Test
    fun whenRawTypeIsNotParameterized_shouldThrowIllegalStateException(){
        try {
            val type: Type = Call::class.java
            val adapter = factory.get( type, emptyArray(), mock() )
            Assert.fail("should have thrown IllegalStateException")
        }catch (ise: IllegalStateException){
            // success
        }
    }

    @Test
    fun whenUpperBoundTypeIsNotNetworkResponse_shouldReturnNull(){
        val type = TypeToken.getParameterized( Call::class.java, String::class.java ).type
        val adapter = factory.get( type, emptyArray(), mock() )
        Assert.assertNull( adapter )
    }

    @Test
    fun whenNetwrokResponseIsNotParameterized_shouldThrowIllegalStateException(){
        try {
            val type = TypeToken.getParameterized( Call::class.java, NetworkResponse::class.java ).type
            factory.get( type, emptyArray(), mock() )
            Assert.fail("should have thrown IllegalStateException")
        }catch (ise: IllegalStateException){
            //success
        }
    }

    @Test
    fun whenNetworkResponseIsOk_shouldCallErrorConverter_withCorrectErrorType(){
        val errorType = String::class.java
        val networkResponseType = TypeToken.getParameterized( NetworkResponse::class.java, Any::class.java, errorType ).type
        val type = TypeToken.getParameterized( Call::class.java, networkResponseType ).type

        val retrofit: Retrofit = mock {
            on { nextResponseBodyConverter<Any>(null, String::class.java, emptyArray() ) } doReturn mock()
        }

        factory.get( type, emptyArray(), retrofit )
        verify(retrofit).nextResponseBodyConverter<Any>(null, errorType, emptyArray() )
    }

    @Test
    fun whenNetworkResponseIsOk_shouldReturnNetworkResponseAdapter(){
        val successType = TypeToken.getParameterized( List::class.java, String::class.java ).type
        val networkResponseType = TypeToken.getParameterized( NetworkResponse::class.java, successType, String::class.java ).type
        val type = TypeToken.getParameterized( Call::class.java, networkResponseType ).type

        val errorBodyConverter: Converter<ResponseBody, Any> = mock()

        val retrofit: Retrofit = mock {
            on { nextResponseBodyConverter<Any>(null, String::class.java, emptyArray() ) } doReturn errorBodyConverter
        }
        val adapter = factory.get( type, emptyArray(), retrofit )

        assertEquals( successType, adapter?.responseType() )
    }
}