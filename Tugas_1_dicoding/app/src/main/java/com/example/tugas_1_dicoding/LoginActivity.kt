package com.example.tugas_1_dicoding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas_1_dicoding.apiService.LoginFetchCallback
import com.example.tugas_1_dicoding.apiService.RetrofitClient
import com.example.tugas_1_dicoding.custom.CustomEditText
import com.example.tugas_1_dicoding.dataClass.LogRequest
import com.example.tugas_1_dicoding.dataClass.LogResponse
import com.example.tugas_1_dicoding.dataClass.LoginResult
import com.example.tugas_1_dicoding.dataClass.User
import com.example.tugas_1_dicoding.databinding.ActivityLoginBinding
import com.example.tugas_1_dicoding.dialogPopUp.DialogPopUp
import com.example.tugas_1_dicoding.sharedPrefHelper.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPrefHelper



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonNavigation(this, binding.tvForgotPassword, PasswordActivity::class.java)
        setupButtonNavigation(this, binding.tvRegister, RegisterActivity::class.java)

        val errorDialog = DialogPopUp(this)
        sharedPref = SharedPrefHelper(this)

        binding.etPassword.mode = CustomEditText.Mode.LOGIN
        binding.etEmail.mode = CustomEditText.Mode.EMAIL

        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.editTextField.text.toString()
            val password = binding.etPassword.editTextField.text.toString()
//            val dataUser  = LogRequest(email,password)
            val dataUser  = LogRequest("azharibastomitest@gmail.com","@Ap0AqPedul1")
            Log.d("asd","ssss")
            fetchLoginUser(dataUser, object : LoginFetchCallback {
                override fun onLoginFetched(login: LoginResult?) {
                    Log.d("asd", login.toString())
                    val user = User(login?.userId.toString(), login?.name.toString(), login?.token.toString(), true)
                    sharedPref.saveUser(user)

                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra("user_data", user)
                    startActivity(intent)
                    finish()
                }
                override fun onError(message: String) {
                    Log.e("Caller", "Failed to fetch story: $message")
                }
            })
        }
    }

    private fun fetchLoginUser (data: LogRequest, callback: LoginFetchCallback) {
        RetrofitClient.instance.loginUser(data).enqueue(object :
            Callback<LogResponse> {
            override fun onResponse(call: Call<LogResponse>, response: Response<LogResponse>) {
                if (response.isSuccessful) {
                    val loginResult = response.body()?.loginResult
                    Log.d("asd",loginResult.toString())
                    callback.onLoginFetched(loginResult)

                } else {
                    callback.onError("Error response: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LogResponse>, t: Throwable) {
                callback.onError("API call failed: ${t.message}")
            }
        })
    }
}
