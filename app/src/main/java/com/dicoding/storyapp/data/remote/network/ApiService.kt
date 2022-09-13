package com.dicoding.storyapp.data.remote.network

import com.dicoding.storyapp.data.model.LoginRequest
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.data.model.StoryRequest
import com.dicoding.storyapp.data.remote.response.GlobalResponse
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("register")
    fun postRegister(
        @Body registerRequest: RegisterRequest
    ): Call<GlobalResponse>

    @POST("login")
    fun postLogin(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    @POST("stories")
    fun postStory(
        @Header("content-type") contentType: String,
        @Header("authorization") token: String,
        @Body storyRequest: StoryRequest
    ): Call<GlobalResponse>

    @POST("stories/guest")
    fun postStoryGuest(
        @Body storyRequest: StoryRequest
    ): Call<GlobalResponse>

    @GET("stories")
    fun getStories(
        @Header("authorization") token: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
        @Query("location") location: Int?
    ):Call<StoriesResponse>
}
