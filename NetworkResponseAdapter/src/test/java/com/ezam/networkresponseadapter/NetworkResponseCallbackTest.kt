package com.ezam.networkresponseadapter

import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.*
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class NetworkResponseCallbackTest {

    @Mock
    private lateinit var networkResponseCall: NetworkResponseCall<Person,String>

    @Mock
    private lateinit var callback: Callback<NetworkResponse<Person,String>>

    @Mock
    private lateinit var errorConverter: Converter<ResponseBody, String>

    private lateinit var networkResponseCallback: NetworkResponseCallback<Person, String>

    @Before
    fun setup(){
        networkResponseCallback = NetworkResponseCallback(
            networkResponseCall,
            callback,
            errorConverter
        )
    }

    @Test
    fun whenResponseIsSucces_launchCallbaclWithSuccesBody(){

        val person = Person( "PERSON", 0)

        networkResponseCallback.onResponse( mock(), Response.success( person ) )

        verify(callback).onResponse(any(), argThat {
            body() == NetworkResponse.Success(person)
        })
    }

    /*
    @Test
    fun successWithBodyNull_andTypeNonNullable_trhowsNPE(){

        try {
            networkResponseCallback.onResponse( mock(), Response.success(null) )
            Assert.fail("should have thrown NullPointerException")
        }catch (npe: NullPointerException){
            // Success
        }
    }*/

    @Test
    fun scuccessWithBodyNull_andTypeNullable_shouldReturnNullable(){

        // especial mocks (person nullable)
        val callback : Callback<NetworkResponse<Person?,String>> = mock()
        val networkResponseCallback = NetworkResponseCallback(mock(), callback, errorConverter)

        networkResponseCallback.onResponse(mock(), Response.success(null) )

        verify( callback ).onResponse( any(), argThat {
            body() == NetworkResponse.Success<Person?>(null)
        })
    }

    @Test
    fun errorWithBodyNull_shouldReturnUnknownError(){

        val response: Response<Person> = mock {
            on{ isSuccessful } doReturn false
            on { errorBody() } doReturn null
        }

        networkResponseCallback.onResponse( mock(), response )

        verify( callback ).onResponse( any(), argThat {
            body() == NetworkResponse.UnknownError(null )
        })
    }

    @Test
    fun emptyErrorBody_shouldReturnNullUnknownError(){

        val errorBody: ResponseBody = mock {
            on{ contentLength() } doReturn 0L
        }

        val response: Response<Person> = mock {
            on{ isSuccessful } doReturn false
            on { errorBody() } doReturn errorBody
        }

        networkResponseCallback.onResponse( mock(), response )

        verify( callback ).onResponse( any(), argThat {
            body() == NetworkResponse.UnknownError(null )
        })
    }

    @Test
    fun onErrorConverterException_shouldRetrunNullUnknownError(){
        val errorConverter : Converter<ResponseBody, String> = mock{
            on{ convert(any()) } doThrow IOException()
        }
        val networkResponseCallback = NetworkResponseCallback(mock(), callback, errorConverter)

        val errorBody: ResponseBody = mock {
            on{ contentLength() } doReturn 1L
        }
        val response: Response<Person> = mock {
            on{ isSuccessful } doReturn false
            on { errorBody() } doReturn errorBody
        }

        networkResponseCallback.onResponse( mock(), response )

        verify( callback ).onResponse( any(), argThat {
            body() == NetworkResponse.UnknownError(null )
        })
    }

    @Test
    fun onError_shouldReturnApiError(){
        val errorConverter : Converter<ResponseBody, String> = mock{
            on{ convert(any()) } doReturn "API ERROR"
        }

        val networkResponseCallback = NetworkResponseCallback(mock(), callback, errorConverter)

        val errorBody: ResponseBody = mock {
            on{ contentLength() } doReturn 1L
        }
        val response: Response<Person> = mock {
            on{ isSuccessful } doReturn false
            on { errorBody() } doReturn errorBody
            on { code() } doReturn 400
        }
        networkResponseCallback.onResponse( mock(), response)

        verify( callback ).onResponse( any(), argThat {
            body() == NetworkResponse.ApiError("API ERROR", 400 )
        })
    }

    @Test
    fun onFailureWithIOException_shouldReturnNetworkError(){
        val throwable = IOException()

        networkResponseCallback.onFailure( mock(), throwable )

        verify( callback ).onResponse( any(), argThat {
            body() == NetworkResponse.NetworkError( throwable )
        })
    }

    @Test
    fun onFailureWithException_shouldReturnUnknownErrorWithThrowable(){
        val throwable = Exception()

        networkResponseCallback.onFailure( mock(), throwable )

        verify( callback ).onResponse( any(), argThat {
            body() == NetworkResponse.UnknownError( throwable )
        })
    }
}

