package com.dicoding.storyapp.utils

import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.data.remote.response.LoginResult

object DummyLogin {
    fun generateDummyLogin(): LoginResponse {
        return LoginResponse(
            LoginResult(
                "Test",
                "1234-abcd",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLW9ldU13bXBCLXNTQmx0NzgiLCJpYXQiOjE2NjQwNjcwNTB9.aof6ofo1I2pqIV00eBpl3ayB8IO5f99pM19GfI0iZ20"
            ),
            false,
            "success"
        )
    }
}