package com.example.tugas_1_dicoding.apiService

import com.example.tugas_1_dicoding.dataClass.DetailStoryResponse
import com.example.tugas_1_dicoding.dataClass.LogRequest
import com.example.tugas_1_dicoding.dataClass.LogResponse
import com.example.tugas_1_dicoding.dataClass.RegRequest
import com.example.tugas_1_dicoding.dataClass.RegResponse
import com.example.tugas_1_dicoding.dataClass.StoryResponse
import com.example.tugas_1_dicoding.dataClass._StoryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("register")
    fun registerUser(@Body request: RegRequest): Call<RegResponse>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun loginUser(@Body request: LogRequest): Call<LogResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int = 0
    ): Call<StoryResponse>

    @GET("stories/{id}")
    fun getStoryById(
        @Header("Authorization") token: String,
        @Path("id") storyId: String
    ): Call<DetailStoryResponse>

    @GET("stories/{id}")
    fun getStory(
        @Header("Authorization") token: String,
        @Path("id") storyId: String
    ): Call<_StoryResponse>
}


