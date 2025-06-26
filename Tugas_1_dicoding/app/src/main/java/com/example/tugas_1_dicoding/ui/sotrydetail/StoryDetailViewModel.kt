package com.example.tugas_1_dicoding.ui.sotrydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tugas_1_dicoding.apiService.RetrofitClient
import com.example.tugas_1_dicoding.dataClass.DetailStoryResponse
import com.example.tugas_1_dicoding.dataClass.Story
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryDetailViewModel : ViewModel() {

    private val _story = MutableLiveData<Story?>()
    val story: LiveData<Story?> get() = _story

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchDetailStory(token: String, id: String) {
        RetrofitClient.instance.getStory(token, id).enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                if (response.isSuccessful) {
                    _story.value = response.body()?.story
                    _error.value = null
                } else {
                    _error.value = "Error response: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _error.value = "API call failed: ${t.message}"
            }
        })
    }
}
