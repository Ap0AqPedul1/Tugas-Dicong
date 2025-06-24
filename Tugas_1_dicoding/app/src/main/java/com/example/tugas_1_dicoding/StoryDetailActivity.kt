package com.example.tugas_1_dicoding

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tugas_1_dicoding.apiService.DetailStoryFetchCallback
import com.example.tugas_1_dicoding.apiService.RetrofitClient
import com.example.tugas_1_dicoding.dataClass.DetailStoryRequest
import com.example.tugas_1_dicoding.dataClass.DetailStoryResponse
import com.example.tugas_1_dicoding.dataClass.Story
import com.example.tugas_1_dicoding.databinding.ActivityStoryDetailBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("EXTRA_ID")
        val token = intent.getStringExtra("EXTRA_TOKEN")

        Log.d("asd", id.toString())
        Log.d("asd", token.toString())

        val paramStories = DetailStoryRequest(id.toString(), token.toString())

        fetchDetailStory(paramStories, object : DetailStoryFetchCallback {
            override fun onStoriesFetched(story: Story?) {
                Log.d("Caller", "Received story: $story")
                binding.textName.text = story?.name
                binding.textDescription.text = story?.description
                binding.textCreatedAt.text = story?.createdAt

                val latitude = story?.lat?.toString() ?: "N/A"
                val longitude = story?.lon?.toString() ?: "N/A"
                binding.textLocation.text = getString(R.string.location_format, latitude, longitude)

                Glide.with(binding.root.context)
                    .load(story?.photoUrl)
                    .fitCenter()
                    .into(binding.imageStory)
            }

            override fun onError(message: String) {
                Log.e("Caller", "Failed to fetch story: $message")
            }
        })


    }

    private fun fetchDetailStory(data: DetailStoryRequest, callback: DetailStoryFetchCallback) {
        RetrofitClient.instance.getStory(data.token, data.id).enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(call: Call<DetailStoryResponse>, response: Response<DetailStoryResponse>) {
                if (response.isSuccessful) {
                    val story = response.body()?.story
                    callback.onStoriesFetched(story)
                } else {
                    callback.onError("Error response: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                callback.onError("API call failed: ${t.message}")
            }
        })
    }


}





