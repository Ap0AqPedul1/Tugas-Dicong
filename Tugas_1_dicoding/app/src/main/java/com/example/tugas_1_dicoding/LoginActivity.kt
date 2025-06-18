package com.example.tugas_1_dicoding

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas_1_dicoding.apiService.AuthService
import com.example.tugas_1_dicoding.apiService.LogRequest
import com.example.tugas_1_dicoding.apiService.RetrofitClient
import com.example.tugas_1_dicoding.databinding.ActivityLoginBinding
import com.example.tugas_1_dicoding.errorDialogPopUp.ErrorDialogPopUp

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authService: AuthService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupButtonNavigation(this, binding.tvForgotPassword, PasswordActivity::class.java)
        setupButtonNavigation(this, binding.tvRegister, RegisterActivity::class.java)

        val errorDialog = ErrorDialogPopUp(this)
        authService = AuthService(RetrofitClient.instance)


        binding.etPassword.mode = CustomEditText.Mode.LOGIN
        binding.etEmail.mode = CustomEditText.Mode.EMAIL

        authService = AuthService(RetrofitClient.instance)

        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.editTextField.text.toString()
            val password = binding.etPassword.editTextField.text.toString()
            val dataUser  = LogRequest(email,password)
            loginUser(dataUser,errorDialog)
            Log.d("asd","ssss")
        }
        }





    private fun loginUser(dataUser: LogRequest, errorDialog: ErrorDialogPopUp){
        authService.loginUser(
            logRequest = dataUser
        ) { result ->
            result.onSuccess {
                errorDialog.show("Sukses","Akun Berhasil Dibuat")
            }.onFailure {
                errorDialog.show("Error:", "${it.message}")
            }
        }
    }
}
