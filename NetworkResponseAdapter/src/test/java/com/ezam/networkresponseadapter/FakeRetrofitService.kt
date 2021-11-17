package com.ezam.networkresponseadapter

import retrofit2.http.GET

interface FakeRetrofitService {
    @GET("listString")
    suspend fun getListString(): NetworkResponse<List<String>, String>
}