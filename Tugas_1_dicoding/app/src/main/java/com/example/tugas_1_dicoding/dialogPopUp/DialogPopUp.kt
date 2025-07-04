package com.example.tugas_1_dicoding.dialogPopUp

import android.app.AlertDialog
import android.content.Context

class DialogPopUp(private val context: Context) {
    fun show(title:String, errorMessage: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(errorMessage)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}
