package com.example.tugas_1_dicoding.apiService

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class RegRequest(
    val name: String,
    val email: String,
    val password: String
)

data class RegResponse(
    val error: Boolean,
    val message: String
)

data class LogRequest(
    val email: String,
    val password: String
)

data class LogResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult?
)

data class LoginResult(
    val userId: String,
    val name: String,
    val token: String
)

data class User(
    val userId: String,
    val name: String,
    val token: String,
    val isLoggedIn: Boolean
)

