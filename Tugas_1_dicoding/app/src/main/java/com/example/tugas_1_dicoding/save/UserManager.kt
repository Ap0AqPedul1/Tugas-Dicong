package com.example.tugas_1_dicoding.save

import android.content.Context

data class User(val userId: String, val name: String, val token: String, val isLoggedIn: Boolean)

object UserManager {

    private const val PREFS_NAME = "UserPrefs"
    private const val KEY_USER_ID = "userId"
    private const val KEY_NAME = "name"
    private const val KEY_TOKEN = "token"
    private const val KEY_LOGIN = "login"

    private var user: User? = null

    fun saveUser(context: Context, user: User) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(KEY_USER_ID, user.userId)
            putString(KEY_NAME, user.name)
            putString(KEY_TOKEN, user.token)
            putBoolean(KEY_LOGIN, user.isLoggedIn)
            apply()
        }
        this.user = user
    }

    fun loadUser(context: Context): User? {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val userId = sharedPref.getString(KEY_USER_ID, null)
        val name = sharedPref.getString(KEY_NAME, null)
        val token = sharedPref.getString(KEY_TOKEN, null)
        val login = sharedPref.getBoolean(KEY_LOGIN, false)

        return if (userId != null && name != null && token != null) {
            val loadedUser = User(userId, name, token, login)
            user = loadedUser
            loadedUser
        } else {
            null
        }
    }

    fun getCurrentUser(): User? {
        return user
    }

    fun clearUser(context: Context) {
        val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        user = null
    }
}
