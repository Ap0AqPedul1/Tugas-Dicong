package com.example.tugas_1_dicoding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas_1_dicoding.databinding.ActivityPasswordBinding

class PasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}