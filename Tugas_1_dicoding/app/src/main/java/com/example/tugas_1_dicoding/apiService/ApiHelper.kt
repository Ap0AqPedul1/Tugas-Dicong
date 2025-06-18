package com.example.tugas_1_dicoding.apiService

import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Interface callback generik
interface ApiCallback<T, E> {
    fun onSuccess(response: T)
    fun onError(errorResponse: E)
    fun onFailure(throwable: Throwable)
}


object ApiHelper {
    private val gson = Gson()

    fun <T, E> executeCall(
        call: Call<T>,
        errorClass: Class<E>,
        callback: ApiCallback<T, E>
    ) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    val errorBodyString = response.errorBody()?.string()
                    if (!errorBodyString.isNullOrEmpty()) {
                        try {
                            val errorResponse = gson.fromJson(errorBodyString, errorClass)
                            callback.onError(errorResponse)
                        } catch (e: Exception) {
                            callback.onFailure(e)
                        }
                    } else {
                        callback.onFailure(Throwable("Unknown error, no error body"))
                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                callback.onFailure(t)
            }
        })
    }
}
