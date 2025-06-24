package com.example.tugas_1_dicoding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_1_dicoding.adapeter.StoryAdapter
import com.example.tugas_1_dicoding.apiService.RetrofitClient
import com.example.tugas_1_dicoding.apiService.StoryFetchCallback
import com.example.tugas_1_dicoding.dataClass.Story
import com.example.tugas_1_dicoding.dataClass.StoryRequest
import com.example.tugas_1_dicoding.dataClass.StoryResponse
import com.example.tugas_1_dicoding.dataClass.User
import com.example.tugas_1_dicoding.databinding.ActivityMainBinding
import com.example.tugas_1_dicoding.uploadPost.PostImageDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter

    private var token  = ""
    private val pageSize = 20
    private var currentPage = 0

    private var isLoading = false
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val user = getUserFromIntent()
        if (user != null) {
            token = user.token
        }


        supportFragmentManager.setFragmentResultListener("requestKey", this) { key, bundle ->
            val result = bundle.getString("result_key")
            Log.d("azhari", "Diterima data dari DialogFragment: $result")
            storyAdapter.clearItems()
            currentPage = 0
        }

        binding.fabUploadPost.setOnClickListener {
            val fragment = PostImageDialogFragment()
            val bundle = Bundle()
            bundle.putString("key_data", user?.token)
            fragment.arguments = bundle
            fragment.show(supportFragmentManager, "PostImageDialog")
        }




        val storyRequest = StoryRequest(token,currentPage, pageSize)
        setupRecyclerView()
        loadStories(storyRequest)


    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager

        storyAdapter = StoryAdapter { story ->
            val intent = Intent(this, StoryDetailActivity::class.java).apply {
                putExtra("EXTRA_ID", story.id)
                putExtra("EXTRA_TOKEN", token)
            }
            startActivity(intent)
        }
        binding.recyclerView.adapter = storyAdapter

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                // Cek jika mendekati 15 item terakhir dan belum loading atau bukan halaman terakhir
                if (!isLoading && !isLastPage && totalItemCount <= lastVisibleItemPosition + 15) {
                    isLoading = true
                    currentPage++
                    Log.d("asd", "data")
                    val storyRequest = StoryRequest(token,currentPage, pageSize)
                    Log.d("asd", storyRequest.toString())
                    loadStories(storyRequest)
                }
            }
        })
    }

    private fun loadStories(storyRequest: StoryRequest){
        fetchStory(storyRequest, object : StoryFetchCallback {
            override fun onStoriesFetched(stories: List<Story>) {
                if (stories.isEmpty()) {
                    Log.d("asd", "kesini???")
                    isLastPage = true
                } else {
                    Log.d("asd", "bentar???")
                    Log.d("asd", stories.toString())
                    storyAdapter.addItems(stories)
                }
                isLoading = false
            }

            override fun onError(message: String) {
                isLoading = false
            }
        })
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_list -> {
                logoutUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logoutUser() {
        // Hapus data user dari SharedPreferences
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }

        // Pindah ke LoginActivity dan bersihkan riwayat activity
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
