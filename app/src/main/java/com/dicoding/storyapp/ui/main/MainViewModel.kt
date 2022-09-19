package com.dicoding.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.model.UserModel
import com.dicoding.storyapp.data.model.UserPreference
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    fun getUser(pref: UserPreference): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout(pref: UserPreference) {
        viewModelScope.launch {
            pref.logout()
        }
    }

}