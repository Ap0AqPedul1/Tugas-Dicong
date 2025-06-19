package com.example.tugas_1_dicoding.apiService

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("register")
    fun registerUser(@Body request: RegRequest): Call<RegResponse>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun loginUser(@Body request: LogRequest): Call<LogResponse>
}


