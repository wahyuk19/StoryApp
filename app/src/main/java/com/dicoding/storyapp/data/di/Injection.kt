package com.dicoding.storyapp.data.di

import com.dicoding.storyapp.data.remote.RemoteDataSource
import com.dicoding.storyapp.data.remote.StoriesRepository
import com.dicoding.storyapp.data.remote.network.ApiConfig

object Injection {
    fun provideRepository(): StoriesRepository {
        val remoteDataSource = RemoteDataSource.getInstance(ApiConfig.provideApiService())
        val apiService = ApiConfig.provideApiService()
        return StoriesRepository.getInstance(remoteDataSource, apiService)
    }
}