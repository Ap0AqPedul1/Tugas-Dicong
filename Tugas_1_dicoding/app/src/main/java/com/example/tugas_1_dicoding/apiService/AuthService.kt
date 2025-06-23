package com.example.tugas_1_dicoding.apiService

import android.util.Log
import com.example.tugas_1_dicoding.dataClass.DetailStoryRequest
import com.example.tugas_1_dicoding.dataClass.DetailStoryResponse
import com.example.tugas_1_dicoding.dataClass.LogRequest
import com.example.tugas_1_dicoding.dataClass.LogResponse
import com.example.tugas_1_dicoding.dataClass.RegRequest
import com.example.tugas_1_dicoding.dataClass.RegResponse
import com.example.tugas_1_dicoding.dataClass.StoryRequest
import com.example.tugas_1_dicoding.dataClass.StoryResponse

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

    fun getStories(
        storyRequest: StoryRequest,
        callback: (Result<StoryResponse>) -> Unit
    ) {

        ApiHelper.executeCall(
            call = apiClient.getStories(storyRequest.token, storyRequest.page, storyRequest.size ),
            errorClass = StoryResponse::class.java,
            callback = object : ApiCallback<StoryResponse, StoryResponse> {
                override fun onSuccess(response: StoryResponse) {
                    Log.d("asd", response.toString())
                    callback(Result.success(response))
                }

                override fun onError(errorResponse: StoryResponse) {
                    callback(Result.failure(Exception(errorResponse.message)))
                }

                override fun onFailure(throwable: Throwable) {
                    callback(Result.failure(throwable))
                }
            }
        )
    }

    fun getDetailStory(
        storyRequest: DetailStoryRequest,
        callback: (Result<DetailStoryResponse>) -> Unit
    ) {

        ApiHelper.executeCall(
            call = apiClient.getStoryById(storyRequest.id, storyRequest.token),
            errorClass = DetailStoryResponse::class.java,
            callback = object : ApiCallback<DetailStoryResponse, DetailStoryResponse> {
                override fun onSuccess(response: DetailStoryResponse) {
                    Log.d("azhari", response.toString())
                    callback(Result.success(response))
                }

                override fun onError(errorResponse: DetailStoryResponse) {
                    callback(Result.failure(Exception(errorResponse.message)))
                }

                override fun onFailure(throwable: Throwable) {
                    callback(Result.failure(throwable))
                }
            }
        )
    }




}
