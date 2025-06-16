package com.example.tugas_1_dicoding

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas_1_dicoding.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonNavigation(this, binding.tvForgotPassword, PasswordActivity::class.java)
        setupButtonNavigation(this, binding.tvRegister, RegisterActivity::class.java)

        binding.etPassword.mode = CustomPasswordEditText.Mode.LOGIN

        val asd = binding.etPassword.getEditTextField().text.toString()
        Log.d("asd", asd.toString())

        binding.btnLogin.setOnClickListener {

        }



    }


}
