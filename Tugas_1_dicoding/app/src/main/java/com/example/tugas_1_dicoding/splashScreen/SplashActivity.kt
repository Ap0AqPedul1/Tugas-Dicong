package com.example.tugas_1_dicoding.splashScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas_1_dicoding.LoginActivity
import com.example.tugas_1_dicoding.MainActivity
import com.example.tugas_1_dicoding.sharedPrefHelper.SharedPrefHelper


@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = SharedPrefHelper(this)
        val user = sharedPref.getUser()

        if (user == null || !user.isLoggedIn) {
            Log.d("asd","data kosong")
            startActivity(Intent(this, LoginActivity::class.java))
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user_data", user)
            startActivity(intent)
        }
        finish()


    }


}
