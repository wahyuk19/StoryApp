package com.dicoding.storyapp.ui.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.storyapp.data.model.UserModel
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.data.remote.StoriesRepository
import com.dicoding.storyapp.data.remote.response.GlobalResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val mStoriesRepository: StoriesRepository,private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun addStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        lat: Float?,
        lon: Float?
    ): LiveData<GlobalResponse> = mStoriesRepository.postStory(token, imageMultipart, description,lat,lon)
}