package com.dicoding.storyapp.data.remote

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.dicoding.storyapp.data.model.LoginRequest
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.data.remote.response.GlobalResponse
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IStoriesRepository {

    fun postRegister(registerRequest: RegisterRequest): LiveData<GlobalResponse>

    fun postLogin(loginRequest: LoginRequest): LiveData<LoginResponse>

    fun postStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        lat: Float?,
        lon: Float?
    ): LiveData<GlobalResponse>

    fun getStoriesByMap(token: String,location: Int) : LiveData<StoriesResponse>

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>>
}