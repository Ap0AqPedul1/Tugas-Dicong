package com.example.tugas_1_dicoding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas_1_dicoding.apiService.AuthService
import com.example.tugas_1_dicoding.dataClass.LogRequest
import com.example.tugas_1_dicoding.apiService.RetrofitClient
import com.example.tugas_1_dicoding.dataClass.User
import com.example.tugas_1_dicoding.custom.CustomEditText
import com.example.tugas_1_dicoding.databinding.ActivityLoginBinding
import com.example.tugas_1_dicoding.dialogPopUp.DialogPopUp
import com.example.tugas_1_dicoding.sharedPrefHelper.SharedPrefHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPrefHelper
    private lateinit var authService: AuthService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonNavigation(this, binding.tvForgotPassword, PasswordActivity::class.java)
        setupButtonNavigation(this, binding.tvRegister, RegisterActivity::class.java)

        val errorDialog = DialogPopUp(this)

        authService = AuthService(RetrofitClient.instance)
        sharedPref = SharedPrefHelper(this)


        binding.etPassword.mode = CustomEditText.Mode.LOGIN
        binding.etEmail.mode = CustomEditText.Mode.EMAIL

        authService = AuthService(RetrofitClient.instance)

        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.editTextField.text.toString()
            val password = binding.etPassword.editTextField.text.toString()
//            val dataUser  = LogRequest(email,password)
            val dataUser  = LogRequest("azharibastomitest@gmail.com","@Ap0AqPedul1")
            loginUser(dataUser,errorDialog)
            Log.d("asd","ssss")
        }
        }

    private fun loginUser(dataUser: LogRequest, errorDialog: DialogPopUp){
        authService.loginUser(
            logRequest = dataUser
        ) { result ->
            result.onSuccess {
                
                binding.etEmail.clearText()
                binding.etPassword.clearText()
                val user = User(it.loginResult?.userId.toString(),
                    it.loginResult?.name.toString(),
                    it.loginResult?.token.toString(),
                    isLoggedIn = true)

                sharedPref.saveUser(user)

                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("user_data", user)
                startActivity(intent)
                finish()

            }.onFailure {
                errorDialog.show("Error:", "${it.message}")
                binding.etPassword.clearText()
            }
        }
    }
}
