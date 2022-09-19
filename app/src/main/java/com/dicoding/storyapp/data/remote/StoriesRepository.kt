package com.dicoding.storyapp.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.storyapp.data.model.LoginRequest
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.data.remote.response.GlobalResponse
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoriesRepository private constructor(private val remoteDataSource: RemoteDataSource): IStoriesRepository {
    companion object{
        @Volatile
        private var instance: StoriesRepository? = null

        fun getInstance(remoteDataSource: RemoteDataSource): StoriesRepository =
            instance ?: synchronized(this){
                instance ?: StoriesRepository(remoteDataSource).apply { instance = this }
            }
    }

    override fun postRegister(registerRequest: RegisterRequest): LiveData<GlobalResponse> {
        val registerRes = MutableLiveData<GlobalResponse>()
        remoteDataSource.postRegister(callback = object : RemoteDataSource.RegisterCallback{
            override fun postRegister(register: GlobalResponse) {
                val registerData = register.let {
                    GlobalResponse(
                        it.error,
                        it.message
                    )
                }
                registerRes.postValue(registerData)
            }

        },registerRequest)
        return registerRes
    }

    override fun postLogin(loginRequest: LoginRequest): LiveData<LoginResponse> {
        val loginRes = MutableLiveData<LoginResponse>()
        remoteDataSource.postLogin(callback = object : RemoteDataSource.LoginCallback{
            override fun postLogin(login: LoginResponse) {
                val loginData = login.let {
                    LoginResponse(
                        it.loginResult,
                        it.error,
                        it.message
                    )
                }
                loginRes.postValue(loginData)
            }
        },loginRequest)
        return loginRes
    }

    override fun postStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        requestBody: RequestBody
    ): LiveData<GlobalResponse> {
        val storyRes = MutableLiveData<GlobalResponse>()
        remoteDataSource.postStory(callback = object : RemoteDataSource.StoryCallback{
            override fun postStory(story: GlobalResponse) {
                val storyData = story.let {
                    GlobalResponse(
                        it.error,
                        it.message
                    )
                }
                storyRes.postValue(storyData)
            }
        },token, imageMultipart, requestBody)
        return storyRes
    }

    override fun getStories(
        token: String,
        page: Int?,
        size: Int?,
        location: Int?
    ): LiveData<StoriesResponse> {
        val storiesRes = MutableLiveData<StoriesResponse>()
        remoteDataSource.getStories(callback = object : RemoteDataSource.StoriesCallback{
            override fun getStories(stories: StoriesResponse) {
                val storiesData = stories.let {
                    StoriesResponse(
                        it.listStory,
                        it.error,
                        it.message
                    )
                }
                storiesRes.postValue(storiesData)
            }
        },token, page, size, location)
        return storiesRes
    }


}