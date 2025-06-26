// MainViewModel.kt
package com.example.tugas_1_dicoding.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tugas_1_dicoding.apiService.RetrofitClient
import com.example.tugas_1_dicoding.dataClass.Story
import com.example.tugas_1_dicoding.dataClass.StoryResponse

class MainViewModel : ViewModel() {

    private var token: String = ""

    private val _stories = MutableLiveData<List<Story>>()
    val stories: LiveData<List<Story>> = _stories

    private val _error = MutableLiveData<String?>()
    val error: MutableLiveData<String?> = _error

    private var currentPage = 1
    private var isLoading = false

    fun setToken(token: String) {
        this.token = token
        // Optionally load data initially if token is ready
        resetPaging()
    }

    fun resetPaging() {
        currentPage = 0
        _stories.value = emptyList()
        loadStories()
    }

    fun loadStories() {
        if (isLoading) return
        isLoading = true

        RetrofitClient.instance.getStories(token, currentPage, 20).enqueue(object : retrofit2.Callback<StoryResponse> {

            override fun onResponse(call: retrofit2.Call<StoryResponse>, response: retrofit2.Response<StoryResponse>) {
                if (response.isSuccessful) {
                    val newStories = response.body()?.listStory ?: emptyList()
                    if (newStories.isNotEmpty()) {
                        val currentList = _stories.value ?: emptyList()
                        _stories.value = currentList + newStories
                        currentPage++
                    }
                } else {
                    Log.d("asd", response.message())
                    _error.value = "Error load stories: ${response.message()}"
                }
                isLoading = false
            }

            override fun onFailure(call: retrofit2.Call<StoryResponse>, t: Throwable) {
                _error.value = "Failure: ${t.message}"
                isLoading = false
            }
        })
    }

    fun clearError() {
        _error.value = null
    }
}
