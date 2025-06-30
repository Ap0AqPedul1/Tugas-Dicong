package com.example.tugas_1_dicoding

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.TextView

fun setupButtonNavigation(context: Context, button: TextView, targetActivity: Class<*>) {
    button.setOnClickListener {
        val intent = Intent(context, targetActivity)
        context.startActivity(intent)
    }
}

fun setupBackButtonNavigation(context: Context, button: TextView, targetActivity: Class<*>) {
    button.setOnClickListener {
        val intent = Intent(context, targetActivity)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        context.startActivity(intent)
        if (context is Activity) {
            context.finish()
        }
    }
}


