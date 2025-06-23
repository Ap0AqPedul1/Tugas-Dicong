package com.example.tugas_1_dicoding.splashScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas_1_dicoding.LoginActivity
import com.example.tugas_1_dicoding.MainActivity
import com.example.tugas_1_dicoding.apiService.RetrofitClient
import com.example.tugas_1_dicoding.dataClass.DetailStoryResponse
import com.example.tugas_1_dicoding.sharedPrefHelper.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val BEARER_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLS1KenFpXzZnSUV5X0FQNksiLCJpYXQiOjE3NTAzMTQ5NTN9.V_GwBrp1-tbTPpI2dR-BndRtOE7SW80LsV0axdT46Js" // Ganti dengan token valid
        private const val STORY_ID = "story--NZmix8cvO7cjBt5"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = SharedPrefHelper(this)
        val user = sharedPref.getUser()

        if (user == null || !user.isLoggedIn) {
            Log.d("asd","data kosong")
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user_data", user)
            startActivity(intent)
        }
        finish()


//        fetchStory()

    }

    private fun fetchStory() {
        RetrofitClient.instance.getStoryById(STORY_ID, BEARER_TOKEN).enqueue(object : Callback<DetailStoryResponse> {
            override fun onResponse(call: Call<DetailStoryResponse>, response: Response<DetailStoryResponse>) {
                if (response.isSuccessful) {
                    val storyResponse = response.body()
                    if (storyResponse != null) {
                        Log.d(TAG, "Error: ${storyResponse.error}")
                        Log.d(TAG, "Message: ${storyResponse.message}")
                        Log.d(TAG, "Story ID: ${storyResponse.data.id}")
                        Log.d(TAG, "Name: ${storyResponse.data.name}")
                        Log.d(TAG, "Description: ${storyResponse.data.description}")
                        Log.d(TAG, "Photo URL: ${storyResponse.data.photoUrl}")
                        Log.d(TAG, "Created At: ${storyResponse.data.createdAt}")
                        Log.d(TAG, "Latitude: ${storyResponse.data.lat}")
                        Log.d(TAG, "Longitude: ${storyResponse.data.lon}")
                    } else {
                        Log.e(TAG, "Response body is null")
                    }
                } else {
                    Log.e(TAG, "Response failed: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                Log.e(TAG, "API call failed: ${t.message}")
            }
        })
    }


}
