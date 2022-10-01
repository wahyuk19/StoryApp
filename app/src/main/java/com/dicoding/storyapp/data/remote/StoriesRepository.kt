package com.dicoding.storyapp.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.data.StoryPagingSource
import com.dicoding.storyapp.data.model.LoginRequest
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.data.remote.network.ApiService
import com.dicoding.storyapp.data.remote.response.GlobalResponse
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoriesRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val apiService: ApiService
) : IStoriesRepository {
    override fun postRegister(registerRequest: RegisterRequest): LiveData<GlobalResponse> {
        val registerRes = MutableLiveData<GlobalResponse>()
        remoteDataSource.postRegister(callback = object : RemoteDataSource.RegisterCallback {
            override fun postRegister(register: GlobalResponse) {
                val registerData = register.let {
                    GlobalResponse(
                        it.error,
                        it.message
                    )
                }
                registerRes.postValue(registerData)
            }

        }, registerRequest)
        return registerRes
    }

    override fun postLogin(loginRequest: LoginRequest): LiveData<LoginResponse> {
        val loginRes = MutableLiveData<LoginResponse>()
        remoteDataSource.postLogin(callback = object : RemoteDataSource.LoginCallback {
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
        }, loginRequest)
        return loginRes
    }

    override fun postStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        lat: Float?,
        lon: Float?
    ): LiveData<GlobalResponse> {
        val storyRes = MutableLiveData<GlobalResponse>()
        remoteDataSource.postStory(callback = object : RemoteDataSource.StoryCallback {
            override fun postStory(story: GlobalResponse) {
                val storyData = story.let {
                    GlobalResponse(
                        it.error,
                        it.message
                    )
                }
                storyRes.postValue(storyData)
            }
        }, token, imageMultipart, description, lat, lon)
        return storyRes
    }

    override fun getStoriesByMap(token: String, location: Int): LiveData<StoriesResponse> {
        val storiesRes = MutableLiveData<StoriesResponse>()
        remoteDataSource.getStoriesByMap(callback = object : RemoteDataSource.StoryMapCallback {
            override fun getStoriesByMap(story: StoriesResponse) {
                val stories = story.let {
                    StoriesResponse(
                        it.listStory,
                        it.error,
                        it.message
                    )
                }
                storiesRes.postValue(stories)
            }
        }, token, location)
        return storiesRes
    }

    override fun getStories(
        token: String
    ): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: StoriesRepository? = null

        fun getInstance(
            remoteDataSource: RemoteDataSource,
            apiService: ApiService
        ): StoriesRepository =
            instance ?: synchronized(this) {
                instance ?: StoriesRepository(remoteDataSource, apiService).apply {
                    instance = this
                }
            }
    }
}