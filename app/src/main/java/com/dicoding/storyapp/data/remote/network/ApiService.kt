package com.dicoding.storyapp.data.remote.network

import com.dicoding.storyapp.data.model.LoginRequest
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.data.remote.response.GlobalResponse
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
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

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float?,
        @Part("lon") lon: Float?
    ): Call<GlobalResponse>

    @GET("stories")
    fun getStoriesByMap(
        @Header("authorization") token: String,
        @Query("location") location: Int?
    ): Call<StoriesResponse>

    @GET("stories")
    suspend fun getPagedStories(
        @Header("authorization") token: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
    ): StoriesResponse
}
