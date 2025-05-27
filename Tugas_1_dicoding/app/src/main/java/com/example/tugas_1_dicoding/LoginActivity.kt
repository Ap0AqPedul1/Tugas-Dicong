package com.example.tugas_1_dicoding

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas_1_dicoding.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup navigasi ke activity lain (asumsi fungsi ini sudah ada)
        setupButtonNavigation(this, binding.tvForgotPassword, PasswordActivity::class.java)
        setupButtonNavigation(this, binding.tvRegister, RegisterActivity::class.java)

        // Listener untuk validasi password saat teks berubah
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val password = s.toString()
                val validationMessage = getPasswordValidationMessage(password)

                if (validationMessage.isNotEmpty()) {
                    if (binding.tvPasswordValidation.visibility == View.GONE) {
                        fadeIn(binding.tvPasswordValidation)
                    }
                    binding.tvPasswordValidation.text = validationMessage
                } else {
                    if (binding.tvPasswordValidation.visibility == View.VISIBLE) {
                        fadeOut(binding.tvPasswordValidation)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Listener untuk perubahan fokus pada EditText password
        binding.etPassword.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Saat fokus hilang, sembunyikan validasi
                if (binding.tvPasswordValidation.visibility == View.VISIBLE) {
                    fadeOut(binding.tvPasswordValidation)
                }
            } else {
                // Saat fokus kembali, cek validasi ulang
                val password = binding.etPassword.text.toString()
                val validationMessage = getPasswordValidationMessage(password)
                if (validationMessage.isNotEmpty()) {
                    if (binding.tvPasswordValidation.visibility == View.GONE) {
                        fadeIn(binding.tvPasswordValidation)
                    }
                    binding.tvPasswordValidation.text = validationMessage
                }
            }
        }
    }

    // Fungsi validasi password
    private fun getPasswordValidationMessage(password: String): String {
        val messages = mutableListOf<String>()

        if (password.length < 6) {
            messages.add("Password harus minimal 6 karakter")
        }
        if (!password.any { it.isDigit() }) {
            messages.add("Password harus mengandung angka")
        }
        if (!password.any { it.isUpperCase() }) {
            messages.add("Password harus mengandung huruf besar")
        }
        if (!password.any { !it.isLetterOrDigit() }) {
            messages.add("Password harus mengandung simbol unik")
        }

        return messages.joinToString("\n")
    }

    // Animasi fade in menggunakan ViewPropertyAnimator
    private fun fadeIn(view: View) {
        view.alpha = 0f
        view.visibility = View.VISIBLE
        view.animate()
            .alpha(1f)
            .setDuration(300)
            .start()
    }

    // Animasi fade out menggunakan ViewPropertyAnimator
    private fun fadeOut(view: View) {
        view.animate()
            .alpha(0f)
            .setDuration(300)
            .withEndAction {
                view.visibility = View.GONE
                view.alpha = 1f // reset alpha untuk penggunaan berikutnya
            }
            .start()
    }

    // Fungsi untuk sembunyikan keyboard secara eksplisit
    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etPassword.windowToken, 0)
    }

    // Override onBackPressed untuk menghilangkan fokus dan sembunyikan validasi saat back ditekan
    override fun onBackPressed() {
        if (binding.etPassword.hasFocus()) {
            binding.etPassword.clearFocus()
            hideKeyboard() // sembunyikan keyboard juga
            if (binding.tvPasswordValidation.visibility == View.VISIBLE) {
                fadeOut(binding.tvPasswordValidation)
            }
        } else {
            super.onBackPressed()
        }
    }
}
