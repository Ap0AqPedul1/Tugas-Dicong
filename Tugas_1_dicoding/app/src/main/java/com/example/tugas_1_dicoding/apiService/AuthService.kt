package com.example.tugas_1_dicoding.apiService

import android.util.Log

class AuthService(private val apiClient: ApiService) {

    fun createUser(
        regRequest: RegRequest,
        callback: (Result<RegResponse>) -> Unit
    ) {

        ApiHelper.executeCall(
            call = apiClient.registerUser(regRequest),
            errorClass = RegResponse::class.java,
            callback = object : ApiCallback<RegResponse, RegResponse> {
                override fun onSuccess(response: RegResponse) {
                    callback(Result.success(response))
                }

                override fun onError(errorResponse: RegResponse) {
                    callback(Result.failure(Exception(errorResponse.message)))
                }

                override fun onFailure(throwable: Throwable) {
                    callback(Result.failure(throwable))
                }
            }
        )
    }

    fun loginUser(
        logRequest: LogRequest,
        callback: (Result<LogResponse>) -> Unit
    ) {

        ApiHelper.executeCall(
            call = apiClient.loginUser(logRequest),
            errorClass = LogResponse::class.java,
            callback = object : ApiCallback<LogResponse, LogResponse> {
                override fun onSuccess(response: LogResponse) {
                    Log.d("asd", response.loginResult.toString())
                    callback(Result.success(response))
                }

                override fun onError(errorResponse: LogResponse) {
                    callback(Result.failure(Exception(errorResponse.message)))
                }

                override fun onFailure(throwable: Throwable) {
                    callback(Result.failure(throwable))
                }
            }
        )
    }
}
