package com.example.tugas_1_dicoding.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas_1_dicoding.ui.main.MainActivity
import com.example.tugas_1_dicoding.ui.forgetPass.PasswordActivity
import com.example.tugas_1_dicoding.ui.register.RegisterActivity
import com.example.tugas_1_dicoding.custom.CustomEditText
import com.example.tugas_1_dicoding.dataClass.LogRequest
import com.example.tugas_1_dicoding.dataClass.User
import com.example.tugas_1_dicoding.databinding.ActivityLoginBinding
import com.example.tugas_1_dicoding.dialogPopUp.DialogPopUp
import com.example.tugas_1_dicoding.setupButtonNavigation
import com.example.tugas_1_dicoding.sharedPrefHelper.SharedPrefHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPref: SharedPrefHelper
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var popupDialog: DialogPopUp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        popupDialog = DialogPopUp(this)
        sharedPref = SharedPrefHelper(this)

        setupButtonNavigation(this, binding.tvForgotPassword, PasswordActivity::class.java)
        setupButtonNavigation(this, binding.tvRegister, RegisterActivity::class.java)

        binding.etPassword.mode = CustomEditText.Mode.LOGIN
        binding.etEmail.mode = CustomEditText.Mode.EMAIL

        loginViewModel.loginResult.observe(this) { login ->
            if (login != null) {
                Log.d("LoginActivity", login.toString())
                val user = User(login.userId, login.name, login.token, true)
                sharedPref.saveUser(user)

                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.putExtra("user_data", user)
                startActivity(intent)
                finish()
            }
        }

        loginViewModel.error.observe(this) { error ->
            error?.let {
                popupDialog.show("Error", it)
                Log.e("LoginActivity", it)
                loginViewModel.clearError()
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.editTextField.text.toString()
            val password = binding.etPassword.editTextField.text.toString()
            val dataUser = LogRequest(email, password)
            loginViewModel.loginUser(dataUser)
        }
    }

}
