package com.dicoding.storyapp.data.remote

import android.util.Log
import com.dicoding.storyapp.data.model.LoginRequest
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.data.remote.network.ApiService
import com.dicoding.storyapp.data.remote.response.GlobalResponse
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource private constructor(private val apiService: ApiService) {
    private val gson = Gson()

    fun postRegister(callback: RegisterCallback,registerRequest: RegisterRequest) {
        val client = apiService.postRegister(registerRequest)
        client.enqueue(object : Callback<GlobalResponse> {
            override fun onResponse(
                call: Call<GlobalResponse>,
                response: Response<GlobalResponse>
            ) {
                if(response.code() == 201){
                    response.body()?.let { callback.postRegister(it) }
                }else{
                    val error = gson.fromJson(response.errorBody()?.string(),GlobalResponse::class.java)
                    error.let { callback.postRegister(it) }
                }
            }

            override fun onFailure(call: Call<GlobalResponse>, t: Throwable) {
                Log.e("RemoteDataSource", t.message.toString())
            }
        })
    }

    fun postLogin(callback: LoginCallback,loginRequest: LoginRequest){
        val client = apiService.postLogin(loginRequest)

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                if(response.code() == 200){
                    response.body()?.let { callback.postLogin(it) }
                }else{
                    val error = gson.fromJson(response.errorBody()?.string(),LoginResponse::class.java)
                    error.let { callback.postLogin(it) }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("RemoteDataSource", t.message.toString())
            }
        })
    }

    fun postStory(callback: StoryCallback,token: String,imageMultipart: MultipartBody.Part,requestBody: RequestBody) {
        val client = apiService.postStory(token, imageMultipart,requestBody)

        client.enqueue(object : Callback<GlobalResponse> {
            override fun onResponse(
                call: Call<GlobalResponse>,
                response: Response<GlobalResponse>
            ) {
                if(response.code() == 201){
                    response.body()?.let { callback.postStory(it) }
                }else{
                    val error = gson.fromJson(response.errorBody()?.string(),GlobalResponse::class.java)
                    error.let { callback.postStory(it) }
                }
            }

            override fun onFailure(call: Call<GlobalResponse>, t: Throwable) {
                Log.e("RemoteDataSource", t.message.toString())
            }
        })
    }

    fun getStories(callback: StoriesCallback, token: String, page: Int?, size: Int?, location: Int?){
        val client = apiService.getStories(token, page, size, location)

        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                if(response.code() == 200){
                    response.body()?.let { callback.getStories(it) }
                }else{
                    val error = gson.fromJson(response.errorBody()?.string(),StoriesResponse::class.java)
                    error.let { callback.getStories(it) }
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                Log.e("RemoteDataSource", t.message.toString())
            }
        })

    }

    interface RegisterCallback{
        fun postRegister(register: GlobalResponse)
    }

    interface LoginCallback{
        fun postLogin(login: LoginResponse)
    }

    interface StoryCallback{
        fun postStory(story: GlobalResponse)
    }

    interface StoriesCallback{
        fun getStories(stories: StoriesResponse)
    }

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(service: ApiService): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(service)
            }
    }
}

