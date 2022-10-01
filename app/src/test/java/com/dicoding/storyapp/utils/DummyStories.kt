package com.dicoding.storyapp.utils

import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.data.remote.response.StoriesResponse

object DummyStories {
    fun generateDummyStories(): StoriesResponse {
        return StoriesResponse(
            listOf(
                ListStoryItem(
                    "https://story-api.dicoding.dev/images/stories/photos-1664544763781_eSQCt-tR.jpg",
                    "2022-09-30T13:32:43.782Z",
                    "Jack",
                    "Test",
                    1.00,
                    "story-W-5rc1PwtKVX2Y2p",
                    2.00
                ),
                ListStoryItem(
                    "https://story-api.dicoding.dev/images/stories/photos-1664561590117_1RB74qNZ.jpg",
                    "2022-09-30T18:13:10.119Z\"",
                    "John",
                    "Test 2",
                    3.00,
                    "story-W-5rc1PwtKVX2Y2p",
                    5.00
                )
            ),

            false,
            "success"
        )
    }
}