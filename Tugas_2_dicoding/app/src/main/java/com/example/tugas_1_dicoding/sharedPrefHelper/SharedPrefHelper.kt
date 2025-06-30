package com.example.tugas_1_dicoding.sharedPrefHelper

import android.content.Context
import com.example.tugas_1_dicoding.dataClass.User
import com.google.gson.Gson

class SharedPrefHelper(context: Context) {
    private val prefs = context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        prefs.edit().putString("user_data", userJson).apply()
    }

    fun getUser(): User? {
        val userJson = prefs.getString("user_data", null)
        return if (userJson != null) {
            gson.fromJson(userJson, User::class.java)
        } else {
            null
        }
    }

    fun clearUser() {
        prefs.edit().remove("user_data").apply()
    }
}
