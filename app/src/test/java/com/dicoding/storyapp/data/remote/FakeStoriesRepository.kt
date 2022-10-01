package com.dicoding.storyapp.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.PagingData
import com.dicoding.storyapp.data.model.LoginRequest
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.data.remote.response.GlobalResponse
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import com.dicoding.storyapp.ui.story.StoryPagingSource
import com.dicoding.storyapp.utils.DummyGlobal
import com.dicoding.storyapp.utils.DummyLogin
import com.dicoding.storyapp.utils.DummyStories
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeStoriesRepository : IStoriesRepository {
    private val dummyGlobalResponse = DummyGlobal.generateDummyGlobal()
    private val dummyLoginResponse = DummyLogin.generateDummyLogin()
    private val dummyStoriesResponse = DummyStories.generateDummyStories()
    override fun postRegister(registerRequest: RegisterRequest): LiveData<GlobalResponse> {
        val response = liveData<GlobalResponse> {
            dummyGlobalResponse
        }
        return response
    }

    override fun postLogin(loginRequest: LoginRequest): LiveData<LoginResponse> {
        val response = liveData<LoginResponse> {
            dummyLoginResponse
        }
        return response
    }

    override fun postStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        lat: Float?,
        lon: Float?
    ): LiveData<GlobalResponse> {
        val response = liveData<GlobalResponse> {
            dummyGlobalResponse
        }
        return response
    }

    override fun getStoriesByMap(token: String, location: Int): LiveData<StoriesResponse> {
        val response = liveData<StoriesResponse> {
            dummyStoriesResponse
        }
        return response
    }

    override fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        val data: PagingData<ListStoryItem> =
            StoryPagingSource.snapshot(dummyStoriesResponse.listStory)
        val res = MutableLiveData<PagingData<ListStoryItem>>()
        res.value = data
        return res
    }

}