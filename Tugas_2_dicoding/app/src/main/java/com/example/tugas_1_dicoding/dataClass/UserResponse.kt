package com.example.tugas_1_dicoding.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class RegRequest(
    val name: String,
    val email: String,
    val password: String
) : Parcelable

@Parcelize
data class RegResponse(
    val error: Boolean,
    val message: String
) : Parcelable

@Parcelize
data class LogRequest(
    val email: String,
    val password: String
) : Parcelable

@Parcelize
data class LogResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult?
) : Parcelable

@Parcelize
data class LoginResult(
    val userId: String,
    val name: String,
    val token: String
) : Parcelable

@Parcelize
data class User(
    val userId: String,
    val name: String,
    private val _token: String,
    val isLoggedIn: Boolean
) : Parcelable {
    val token: String
        get() = "Bearer $_token"
}

@Parcelize
data class Item(val id: String, val title: String) : Parcelable

@Parcelize
data class StoryRequest(
    val token: String,
    val page: Int,
    val size: Int,
    val location: Int = 0
) : Parcelable

@Parcelize
data class StoryResponse(
    val error: Boolean,
    val message: String,
    val listStory: List<Story>
) : Parcelable


@Parcelize
data class DetailStoryResponse(
    val error: Boolean,
    val message: String,
    val story: Story
) :Parcelable



@Parcelize
data class DetailStoryRequest(
    val id: String,
    val token: String,
) : Parcelable


@Parcelize
data class Story(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double?,     // nullable if it might be null
    val lon: Double?      // nullable if it might be null
) : Parcelable

@Parcelize
data class UploadResponse(
    val error: Boolean,
    val message: String
) : Parcelable

@Parcelize
data class UploadRequest(
    var token: String,
    var description: String,
    var lat: Double?,
    var lon: Double?,
    var photoFile: File
) :Parcelable

@Parcelize
data class ErrorResponse(
    val error: Boolean,
    val message: String
) : Parcelable




