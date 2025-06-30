// MainActivity.kt
package com.example.tugas_1_dicoding.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugas_1_dicoding.R
import com.example.tugas_1_dicoding.ui.sotrydetail.StoryDetailActivity
import com.example.tugas_1_dicoding.adapeter.StoryAdapter
import com.example.tugas_1_dicoding.dataClass.User
import com.example.tugas_1_dicoding.databinding.ActivityMainBinding
import com.example.tugas_1_dicoding.dialogPopUp.DialogPopUp
import com.example.tugas_1_dicoding.sharedPrefHelper.SharedPrefHelper
import com.example.tugas_1_dicoding.ui.login.LoginActivity
import com.example.tugas_1_dicoding.uploadPost.PostImageDialogFragment

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter
    private lateinit var sharedPref: SharedPrefHelper


    private val viewModel: MainViewModel by viewModels()

    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = getUserFromIntent()
        if (user != null) {
            token = user.token
        }

        viewModel.setToken(token)
        sharedPref = SharedPrefHelper(this)

        setupRecyclerView()

        viewModel.stories.observe(this) { stories ->
            storyAdapter.addItems(stories)
        }

        // Observe error LiveData untuk menampilkan dialog popup
        viewModel.error.observe(this) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                val popupDialog = DialogPopUp(this)
                popupDialog.show("Error", errorMessage)
                viewModel.clearError()
            }
        }

        supportFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            val result = bundle.getString("result_key")
            Log.d("azhari", "Diterima data dari DialogFragment: $result")
            storyAdapter.clearItems()
            viewModel.resetPaging()
        }

        binding.fabUploadPost.setOnClickListener {
            val fragment = PostImageDialogFragment()
            val bundle = Bundle()
            bundle.putString("key_data", token)
            fragment.arguments = bundle
            fragment.show(supportFragmentManager, "PostImageDialog")
        }
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

        binding.recyclerView.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (totalItemCount <= lastVisibleItemPosition + 15) {
                    viewModel.loadStories()
                }
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
        sharedPref.clearUser()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


}
