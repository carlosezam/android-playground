package com.ezam.networkresponseadapter

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.*
import org.junit.Assert.*

import retrofit2.Retrofit

class NetworkResponseTest {

    @get:Rule
    val mockWebServer = MockWebServer()

    val contentType = "application/json".toMediaType()
    //val contentType = "text/plain".toMediaType()

    private val json by lazy {
        Json { isLenient = true }
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory( json.asConverterFactory( contentType ) )
            .build()
    }

    private val service by lazy {
        retrofit.create( FakeRetrofitService::class.java )
    }

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    val listString = Json.encodeToString( listOf("A","B","C") )

    @Test
    fun whenRensposeCode200_shoulReturnSuccess() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody( listString )

        mockWebServer.enqueue( mockResponse )

        val response = service.getListString()

        val expected = NetworkResponse.Success( listOf("A","B","C") )

        Assert.assertEquals( expected, response )
    }

    @Test
    fun whenResponseCode400_shouldReturnApiError() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setBody( Json{isLenient=true}.encodeToString("Bad Request") )

        mockWebServer.enqueue( mockResponse )

        val response = service.getListString()

        val expected = NetworkResponse.ApiError("Bad Request", 400 )

        Assert.assertEquals( expected, response )
    }

    @Test
    fun whenResponseCode400_AndEmptyBody_shouldReturnUnknownError() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(400)
            .setBody( "" )


        mockWebServer.enqueue( mockResponse )

        val response = service.getListString()

        val expected = NetworkResponse.UnknownError(null )

        Assert.assertEquals( expected, response )
    }

    @Test
    fun whenNetworkError_shouldReturnNetworkError() = runBlocking {
        val mockResponse = MockResponse()
            .setSocketPolicy(SocketPolicy.DISCONNECT_AT_START)

        mockWebServer.enqueue( mockResponse )

        val response = service.getListString()

        Assert.assertTrue( response is NetworkResponse.NetworkError)
    }
}