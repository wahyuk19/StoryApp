package com.dicoding.storyapp.data.model

import okhttp3.MultipartBody

data class StoryRequest(
    var description: String,
    var photo: MultipartBody.Part,
    var lat: Float?,
    var lon: Float?

)
