package com.example.tugas_1_dicoding.uploadPost

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

object ImageUtils {
    fun resizeImageIfNeeded(bitmap: Bitmap): Bitmap {
        val maxFileSize = 1 * 1024 * 1024 // 1 MB bytes

        // Cek ukuran bitmap saat dikompres dengan kualitas 100 (full quality)
        val initialStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, initialStream)
        val initialSize = initialStream.size()

        // Jika masih di bawah batas maksimum (1 MB), return bitmap asli langsung
        if (initialSize <= maxFileSize) {
            return bitmap
        }

        // Jika lebih besar, compress berulang menurunkan kualitas sampai ukuran sesuai
        var compressQuality = 100
        var streamLength: Int

        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            streamLength = bmpStream.size()
            compressQuality -= 5
        } while (streamLength > maxFileSize && compressQuality > 5)

        val finalStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, finalStream)
        val byteArray = finalStream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

}
