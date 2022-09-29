package com.dicoding.storyapp.utils

import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.LoginResult
import com.dicoding.storyapp.data.remote.response.StoriesResponse

object DummyStories {
    fun generateDummyStories(): StoriesResponse {
        return StoriesResponse(
            ListStoryItem(),
            false,
            "success"
        )
    }
}