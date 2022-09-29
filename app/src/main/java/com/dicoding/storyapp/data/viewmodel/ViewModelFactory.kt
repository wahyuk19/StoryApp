package com.dicoding.storyapp.data.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.di.Injection
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.data.remote.StoriesRepository
import com.dicoding.storyapp.ui.addstory.AddStoryViewModel
import com.dicoding.storyapp.ui.auth.login.LoginViewModel
import com.dicoding.storyapp.ui.auth.signup.SignUpViewModel
import com.dicoding.storyapp.ui.main.MainViewModel
import com.dicoding.storyapp.ui.maps.MapsViewModel
import com.dicoding.storyapp.ui.story.StoryViewModel

class ViewModelFactory private constructor(private val mStoriesRepository: StoriesRepository,private val pref:UserPreference) :
    ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(pref:UserPreference): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(),pref).apply {
                    instance = this
                }
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(mStoriesRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(mStoriesRepository,pref) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(mStoriesRepository,pref) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(mStoriesRepository,pref) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(mStoriesRepository,pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}