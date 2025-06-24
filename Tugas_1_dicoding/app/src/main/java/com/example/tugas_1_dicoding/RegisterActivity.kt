package com.example.tugas_1_dicoding

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.tugas_1_dicoding.apiService.CreateUserFetchCallback
import com.example.tugas_1_dicoding.apiService.DetailStoryFetchCallback
import com.example.tugas_1_dicoding.dataClass.RegRequest
import com.example.tugas_1_dicoding.apiService.RetrofitClient
import com.example.tugas_1_dicoding.custom.CustomEditText
import com.example.tugas_1_dicoding.dataClass.RegResponse
import com.example.tugas_1_dicoding.dataClass.Story
import com.example.tugas_1_dicoding.databinding.ActivityRegisterBinding
import com.example.tugas_1_dicoding.dialogPopUp.DialogPopUp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackButtonNavigation(this,binding.tvLogin, LoginActivity::class.java )

        val popupDialog = DialogPopUp(this)

        binding.etName.mode = CustomEditText.Mode.NAME
        binding.etEmail.mode = CustomEditText.Mode.EMAIL
        binding.etPassword.mode = CustomEditText.Mode.CREATE_ACCOUNT

        binding.registerButton.setOnClickListener{
            val name = binding.etName.editTextField.text.toString()
            val email = binding.etEmail.editTextField.text.toString()
            val password = binding.etPassword.editTextField.text.toString()

            val dataUser  = RegRequest(name,email,password)
            fetchCreateUser(dataUser, object : CreateUserFetchCallback {
                override fun onCreateFetched(create: String?) {
                    Log.d("Caller", "Received story: $create")
                    popupDialog.show("Success: ", create.toString())
                    binding.etName.clearText()
                    binding.etEmail.clearText()
                    binding.etPassword.clearText()
                }
                override fun onError(message: String) {
                    Log.e("Caller", "Failed to fetch story: $message")
                    popupDialog.show("Error: ", message)
                }
            })
        }

    }

    private fun fetchCreateUser (data: RegRequest, callback: CreateUserFetchCallback) {
        RetrofitClient.instance.registerUser(data).enqueue(object :
            Callback<RegResponse> {
            override fun onResponse(call: Call<RegResponse>, response: Response<RegResponse>) {
                if (response.isSuccessful) {
                    val massage = response.body()?.message
                    callback.onCreateFetched(massage)
                } else {
                    callback.onError("Error response: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<RegResponse>, t: Throwable) {
                callback.onError("API call failed: ${t.message}")
            }
        })
    }

}