package com.dicoding.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.storyapp.data.model.UserModel
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.data.remote.StoriesRepository
import com.dicoding.storyapp.data.remote.response.StoriesResponse

class MapsViewModel(
    private val storiesRepository: StoriesRepository,
    private val pref: UserPreference
) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getStories(
        token: String,
        location: Int
    ): LiveData<StoriesResponse> = storiesRepository.getStoriesByMap(token, location)
}