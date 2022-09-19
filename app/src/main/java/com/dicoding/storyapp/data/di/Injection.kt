package com.dicoding.storyapp.data.di

import android.content.Context
import com.dicoding.storyapp.data.remote.RemoteDataSource
import com.dicoding.storyapp.data.remote.StoriesRepository
import com.dicoding.storyapp.data.remote.network.ApiConfig

object Injection {
    fun provideRepository(context:Context): StoriesRepository {
        val remoteDataSource = RemoteDataSource.getInstance(ApiConfig.provideApiService())
        return StoriesRepository.getInstance(remoteDataSource)
    }
}