package com.example.tugas_1_dicoding.ui.sotrydetail

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tugas_1_dicoding.R
import com.example.tugas_1_dicoding.databinding.ActivityStoryDetailBinding
import com.example.tugas_1_dicoding.dialogPopUp.DialogPopUp


class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding
    private val viewModel: StoryDetailViewModel by viewModels()
    private lateinit var popupDialog: DialogPopUp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        popupDialog = DialogPopUp(this)

        val id = intent.getStringExtra("EXTRA_ID") ?: ""
        val token = intent.getStringExtra("EXTRA_TOKEN") ?: ""

        // Observing story data
        viewModel.story.observe(this) { story ->
            if (story != null) {
                binding.textName.text = story.name
                binding.textDescription.text = story.description
                binding.textCreatedAt.text = story.createdAt

                val latitude = story.lat?.toString() ?: "N/A"
                val longitude = story.lon?.toString() ?: "N/A"
                binding.textLocation.text = getString(R.string.location_format, latitude, longitude)

                Glide.with(binding.root.context)
                    .load(story.photoUrl)
                    .fitCenter()
                    .into(binding.imageStory)
            }
        }

        viewModel.error.observe(this) { errorMessage ->
            errorMessage?.let {
                popupDialog.show("Error", it)
                Log.e("Caller", it)
            }
        }

        viewModel.fetchDetailStory(token, id)
    }
}






