package com.dicoding.storyapp.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.model.LoginRequest
import com.dicoding.storyapp.data.model.UserModel
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.data.remote.StoriesRepository
import com.dicoding.storyapp.data.remote.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {

    fun saveSession(pref: UserPreference, userModel: UserModel) {
        viewModelScope.launch {
            pref.login(userModel)
        }
    }

    fun login(request: LoginRequest): LiveData<LoginResponse> = storiesRepository.postLogin(request)
}