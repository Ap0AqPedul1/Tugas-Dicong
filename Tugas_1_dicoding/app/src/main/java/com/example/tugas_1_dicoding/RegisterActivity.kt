package com.example.tugas_1_dicoding

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas_1_dicoding.apiService.AuthService
import com.example.tugas_1_dicoding.apiService.RegRequest
import com.example.tugas_1_dicoding.apiService.RetrofitClient
import com.example.tugas_1_dicoding.custom.CustomEditText
import com.example.tugas_1_dicoding.databinding.ActivityRegisterBinding
import com.example.tugas_1_dicoding.dialogPopUp.DialogPopUp

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBackButtonNavigation(this,binding.tvLogin, LoginActivity::class.java )

        val errorDialog = DialogPopUp(this)
        authService = AuthService(RetrofitClient.instance)


        binding.etName.mode = CustomEditText.Mode.NAME
        binding.etEmail.mode = CustomEditText.Mode.EMAIL
        binding.etPassword.mode = CustomEditText.Mode.CREATE_ACCOUNT



        binding.registerButton.setOnClickListener{
            val name = binding.etName.editTextField.text.toString()
            val email = binding.etEmail.editTextField.text.toString()
            val password = binding.etPassword.editTextField.text.toString()
            val dataUser  = RegRequest(name,email,password)
            createUser(dataUser,errorDialog)
            Log.d("asd","ssss")
        }
    }

    private fun createUser(dataUser: RegRequest, errorDialog:DialogPopUp){
        authService.createUser(
            regRequest = dataUser
        ) { result ->
            result.onSuccess {
                errorDialog.show("Sukses","Akun Berhasil Dibuat")
                binding.etEmail.clearText()
                binding.etName.clearText()
                binding.etPassword.clearText()
            }.onFailure {
                binding.etPassword.clearText()
                errorDialog.show("Error:", "${it.message}")
            }
        }
    }





}