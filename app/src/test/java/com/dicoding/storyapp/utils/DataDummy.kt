package com.dicoding.storyapp.utils

import com.dicoding.storyapp.data.model.UserModel

object DataDummy {
    fun generateDummyMain(): UserModel {
        return UserModel(
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLW9ldU13bXBCLXNTQmx0NzgiLCJpYXQiOjE2NjQwNjcwNTB9.aof6ofo1I2pqIV00eBpl3ayB8IO5f99pM19GfI0iZ20",
            "Jack",
            "jack@email.com",
            "123456",
            true
        )
    }
}