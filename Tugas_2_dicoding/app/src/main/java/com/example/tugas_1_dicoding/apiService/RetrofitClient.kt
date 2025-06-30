package com.example.tugas_1_dicoding.apiService

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val instance: RetrofitService by lazy {
        retrofit.create(RetrofitService::class.java)
    }

    fun <T> getErrorConverter(clazz: Class<T>): Converter<ResponseBody, T> {
        return retrofit.responseBodyConverter(clazz, arrayOf())
    }
}

