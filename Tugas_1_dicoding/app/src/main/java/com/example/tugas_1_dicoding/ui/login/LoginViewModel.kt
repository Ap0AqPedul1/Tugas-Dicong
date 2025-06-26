package com.example.tugas_1_dicoding.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tugas_1_dicoding.apiService.RetrofitClient
import com.example.tugas_1_dicoding.dataClass.ErrorResponse
import com.example.tugas_1_dicoding.dataClass.LogRequest
import com.example.tugas_1_dicoding.dataClass.LogResponse
import com.example.tugas_1_dicoding.dataClass.LoginResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult?>()
    val loginResult: LiveData<LoginResult?> = _loginResult

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loginUser(data: LogRequest) {
        RetrofitClient.instance.loginUser(data).enqueue(object :
            Callback<LogResponse> {
            override fun onResponse(call: Call<LogResponse>, response: Response<LogResponse>) {
                if (response.isSuccessful) {
                    _loginResult.value = response.body()?.loginResult
                } else {
                    val converter = RetrofitClient.getErrorConverter(ErrorResponse::class.java)
                    val errorResponse = response.errorBody()?.let { converter.convert(it) }?.message
                    _error.value = "Error response: $errorResponse"
                }
            }

            override fun onFailure(call: Call<LogResponse>, t: Throwable) {
                _error.value = "API call failed: ${t.message}"
            }
        })
    }

    fun clearError() {
        _error.value = null
    }
}
