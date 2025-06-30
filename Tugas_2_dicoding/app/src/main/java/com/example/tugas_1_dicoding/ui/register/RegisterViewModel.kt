package com.example.tugas_1_dicoding.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tugas_1_dicoding.apiService.RetrofitClient
import com.example.tugas_1_dicoding.dataClass.ErrorResponse
import com.example.tugas_1_dicoding.dataClass.RegRequest
import com.example.tugas_1_dicoding.dataClass.RegResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _registerResult = MutableLiveData<Result<String>>()
    val registerResult: LiveData<Result<String>> get() = _registerResult

    fun registerUser(data: RegRequest) {
        RetrofitClient.instance.registerUser(data).enqueue(object : Callback<RegResponse> {
            override fun onResponse(call: Call<RegResponse>, response: Response<RegResponse>) {
                if (response.isSuccessful) {
                    val message = response.body()?.message ?: "Success"
                    _registerResult.postValue(Result.success(message))
                } else {
                    val converter = RetrofitClient.getErrorConverter(ErrorResponse::class.java)
                    val errorResponse = response.errorBody()?.let { converter.convert(it) }?.message ?: "Unknown error"
                    _registerResult.postValue(Result.failure(Exception("Error response: $errorResponse")))
                }
            }

            override fun onFailure(call: Call<RegResponse>, t: Throwable) {
                _registerResult.postValue(Result.failure(Exception("API call failed: ${t.message}")))
            }
        })
    }
}
