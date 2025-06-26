package com.example.tugas_1_dicoding.uploadPost

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tugas_1_dicoding.apiService.RetrofitClient
import com.example.tugas_1_dicoding.dataClass.ErrorResponse
import com.example.tugas_1_dicoding.dataClass.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadPostViewModel : ViewModel() {

    private val _imageBitmap = MutableLiveData<Bitmap?>()
    val imageBitmap: LiveData<Bitmap?> = _imageBitmap

    private val _uploadResult = MutableLiveData<String?>()
    val uploadResult: MutableLiveData<String?> = _uploadResult

    private val _uploadError = MutableLiveData<String?>()
    val uploadError: LiveData<String?> = _uploadError

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    fun setImageBitmap(bitmap: Bitmap) {
        _imageBitmap.value = bitmap
    }

    fun clearImageBitmap() {
        _imageBitmap.value = null
    }


    private val _token = MutableLiveData<String>()
    val token: LiveData<String> = _token

    fun setToken(token: String) {
        _token.value = token
    }

    fun setDescription(desc: String) {
        _description.value = desc
    }

    fun uploadImage(
        token: String,
        imagePart: MultipartBody.Part,
        description: RequestBody,)
    {

        RetrofitClient.instance.uploadImage(token,imagePart, description, null, null).enqueue(object :
            Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                if (response.isSuccessful) {
                    _uploadResult.value = response.body()?.message.toString()

                } else {

                    val converter = RetrofitClient.getErrorConverter(ErrorResponse::class.java)
                    val errorResponse = response.errorBody()?.let { converter.convert(it) }?.message
                    _uploadError.value = errorResponse
                        Log.d("asd",errorResponse.toString())
                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                _uploadError.value = t.toString()
            }
        })
    }
}

