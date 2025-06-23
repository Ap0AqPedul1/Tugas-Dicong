package com.example.tugas_1_dicoding.dataClass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
data class _StoryResponse(
    val error: Boolean,
    val message: String,
    val story: Story
) :Parcelable

data class _Story(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double,
    val lon: Double
)


data class DetailStoryRequest(
    val id: String,
    val token: String,
)


data class DetailStoryResponse(
    val error: Boolean,
    val message: String,
    val data: asd
)

data class asd(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val createdAt: String,
    val lat: Double?,     // nullable if it might be null
    val lon: Double?      // nullable if it might be null
)

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





