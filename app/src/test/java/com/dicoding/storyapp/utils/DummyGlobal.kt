package com.dicoding.storyapp.utils

import com.dicoding.storyapp.data.remote.response.GlobalResponse

object DummyGlobal {
    fun generateDummyGlobal(): GlobalResponse {
        return GlobalResponse(
            false,
            "Success"
        )
    }
}