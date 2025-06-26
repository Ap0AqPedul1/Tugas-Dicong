package com.example.tugas_1_dicoding.ui.splashScreen


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.tugas_1_dicoding.databinding.ActivitySplashBinding
import com.example.tugas_1_dicoding.sharedPrefHelper.SharedPrefHelper
import com.example.tugas_1_dicoding.ui.login.LoginActivity
import com.example.tugas_1_dicoding.ui.main.MainActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var progressStatus = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var scaleAnimation: ScaleAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = SharedPrefHelper(this)
        val user = sharedPref.getUser()


        scaleAnimation = ScaleAnimation(
            1.0f, 1.1f,
            1.0f, 1.1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 500
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }


        binding.imageViewSplash.startAnimation(scaleAnimation)


        Thread {
            while (progressStatus < 100) {
                progressStatus++
                handler.post {
                    binding.progressBarCircular.progress = progressStatus
                }
                Thread.sleep(50)
            }
            handler.post {
                binding.imageViewSplash.clearAnimation()
                if (user == null || !user.isLoggedIn) {
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("user_data", user)
                    startActivity(intent)
                }
                finish()

            }
        }.start()
    }
}
