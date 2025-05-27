package com.example.tugas_1_dicoding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas_1_dicoding.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackButtonNavigation(this,binding.tvLogin, LoginActivity::class.java )
    }



}