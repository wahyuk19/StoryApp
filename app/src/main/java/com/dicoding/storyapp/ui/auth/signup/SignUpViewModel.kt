package com.dicoding.storyapp.ui.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.data.model.UserModel
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.data.remote.StoriesRepository
import com.dicoding.storyapp.data.remote.response.GlobalResponse
import kotlinx.coroutines.launch

class SignUpViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {
    fun saveUser(pref: UserPreference,user: UserModel){
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun register(register: RegisterRequest): LiveData<GlobalResponse> = storiesRepository.postRegister(register)
}