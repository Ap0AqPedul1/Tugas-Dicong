package com.example.tugas_1_dicoding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_1_dicoding.adapeter.StoryAdapter
import com.example.tugas_1_dicoding.apiService.AuthService
import com.example.tugas_1_dicoding.apiService.RetrofitClient
import com.example.tugas_1_dicoding.apiService.StoryFetchCallback
import com.example.tugas_1_dicoding.dataClass.Story
import com.example.tugas_1_dicoding.dataClass.StoryRequest
import com.example.tugas_1_dicoding.dataClass.StoryResponse
import com.example.tugas_1_dicoding.dataClass.User
import com.example.tugas_1_dicoding.databinding.ActivityMainBinding
import com.example.tugas_1_dicoding.uploadPost.PostFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: StoryAdapter

    private val pageSize = 15
    private var currentPage = 0
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = getUserFromIntent()

        adapter = StoryAdapter { story ->
            val intent = Intent(this, StoryDetailActivity::class.java).apply {
                putExtra("EXTRA_ID", story.id)
                putExtra("EXTRA_TOKEN", user?.token)
            }
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = rv.layoutManager as LinearLayoutManager
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                // Jika belum loading dan posisi scroll sudah mencapai item ke 6 dari data yg sudah dimuat
                if (!isLoading && lastVisiblePosition >= totalItemCount - 4) {
                    loadNextPage(user?.token.toString())
                }
            }
        })

        loadNextPage(user?.token.toString())  // load page pertama
    }

    private fun loadNextPage(token:String) {
        isLoading = true

        currentPage * pageSize
        val paramStories = StoryRequest(token,currentPage,pageSize)

        binding.fabUploadPost.setOnClickListener {
            val fragment = PostFragment()
            fragment.show(supportFragmentManager, "upload_post_fragment")
        }

        fetchStory(paramStories, object : StoryFetchCallback {
            override fun onStoriesFetched(stories: List<Story>) {
                if (stories.isNotEmpty()) {
                    adapter.addItems(stories)

                    // Cek apakah ada item ke-13 dari keseluruhan yang sudah dimuat
                    val totalItems = (currentPage + 1) * pageSize
                    if (totalItems >= 13) {
                        Log.d(
                            "PaginationLog",
                            "Data sudah mencapai item ke-13 atau lebih. Total Item: $totalItems"
                        )
                    }
                    currentPage++
                }
                Log.d("PaginationLog", stories.toString())
            }

            override fun onError(message: String) {
                Log.e("StoryError", message)
            }
        })

        isLoading = false
    }

    private fun fetchStory(storyRequest: StoryRequest, callback: StoryFetchCallback) {
        RetrofitClient.instance.getStories(storyRequest.token, storyRequest.page, storyRequest.size)
            .enqueue(object : Callback<StoryResponse> {
                override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                    if (response.isSuccessful) {
                        val storyResponse = response.body()
                        if (storyResponse != null) {
                            callback.onStoriesFetched(storyResponse.listStory)
                        } else {
                            callback.onError("Response body or listStory is null")
                        }
                    } else {
                        callback.onError("Response failed: ${response.code()} - ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    callback.onError("Network request failed: ${t.message}")
                }
            })
    }

    private fun getUserFromIntent(): User? {
        return intent.getParcelableExtra("user_data")
    }



}
