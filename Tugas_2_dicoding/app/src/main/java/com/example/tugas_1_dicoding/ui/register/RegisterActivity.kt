package com.example.tugas_1_dicoding.ui.register

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas_1_dicoding.custom.CustomEditText
import com.example.tugas_1_dicoding.dataClass.RegRequest
import com.example.tugas_1_dicoding.databinding.ActivityRegisterBinding
import com.example.tugas_1_dicoding.dialogPopUp.DialogPopUp
import com.example.tugas_1_dicoding.setupBackButtonNavigation
import com.example.tugas_1_dicoding.ui.login.LoginActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private lateinit var popupDialog: DialogPopUp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackButtonNavigation(this,binding.tvLogin, LoginActivity::class.java )
        popupDialog = DialogPopUp(this)

        binding.etName.mode = CustomEditText.Mode.NAME
        binding.etEmail.mode = CustomEditText.Mode.EMAIL
        binding.etPassword.mode = CustomEditText.Mode.CREATE_ACCOUNT

        binding.registerButton.setOnClickListener {
            val name = binding.etName.editTextField.text.toString()
            val email = binding.etEmail.editTextField.text.toString()
            val password = binding.etPassword.editTextField.text.toString()

            val dataUser = RegRequest(name, email, password)
            viewModel.registerUser(dataUser)
        }

        viewModel.registerResult.observe(this) { result ->
            result.onSuccess { message ->
                Log.d("RegisterActivity", "Success: $message")
                popupDialog.show("Success: ", message)
                binding.etName.clearText()
                binding.etEmail.clearText()
                binding.etPassword.clearText()
            }
            result.onFailure { error ->
                Log.e("RegisterActivity", "Error: ${error.message}")
                popupDialog.show("Error: ", error.message ?: "Unknown error")
            }
        }
    }
}
