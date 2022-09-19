package com.dicoding.storyapp.data.remote

import androidx.lifecycle.LiveData
import com.dicoding.storyapp.data.model.LoginRequest
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.data.remote.response.GlobalResponse
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IStoriesRepository {

    fun postRegister(registerRequest: RegisterRequest): LiveData<GlobalResponse>

    fun postLogin(loginRequest: LoginRequest): LiveData<LoginResponse>

    fun postStory(token: String,imageMultipart: MultipartBody.Part,requestBody: RequestBody): LiveData<GlobalResponse>

    fun getStories(token: String,page: Int?,size: Int?,location: Int?): LiveData<StoriesResponse>
}