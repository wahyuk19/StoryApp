package com.dicoding.storyapp.ui.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.data.remote.StoriesRepository
import com.dicoding.storyapp.data.remote.response.GlobalResponse

class SignUpViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {
    fun register(register: RegisterRequest): LiveData<GlobalResponse> =
        storiesRepository.postRegister(register)
}