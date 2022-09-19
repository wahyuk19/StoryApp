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

class AddStoryViewModel(private val mStoriesRepository: StoriesRepository): ViewModel() {
    fun getUser(pref: UserPreference): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun addStory(token: String,imageMultipart: MultipartBody.Part,requestBody: RequestBody): LiveData<GlobalResponse> = mStoriesRepository.postStory(token, imageMultipart, requestBody)
}