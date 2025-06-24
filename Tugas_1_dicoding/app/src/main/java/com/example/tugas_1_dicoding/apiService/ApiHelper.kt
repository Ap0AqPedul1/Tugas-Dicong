package com.example.tugas_1_dicoding.apiService

import com.example.tugas_1_dicoding.dataClass.LoginResult
import com.example.tugas_1_dicoding.dataClass.Story

// Interface callback generik
interface ApiCallback<T, E> {
    fun onSuccess(response: T)
    fun onError(errorResponse: E)
    fun onFailure(throwable: Throwable)
}

interface StoryFetchCallback {
    fun onStoriesFetched(stories: List<Story>)
    fun onError(message: String)
}

interface DetailStoryFetchCallback {
    fun onStoriesFetched(story: Story?)
    fun onError(message: String)
}

interface LoginFetchCallback {
    fun onLoginFetched(login: LoginResult?)
    fun onError(message: String)
}

interface CreateUserFetchCallback {
    fun onCreateFetched(create: String?)
    fun onError(message: String)
}

interface UploadFetchCallback {
    fun onUploadFetched(upload: String)
    fun onError(message: String)
}
