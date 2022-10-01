package com.dicoding.storyapp.ui.addstory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.data.remote.StoriesRepository
import com.dicoding.storyapp.data.remote.response.GlobalResponse
import com.dicoding.storyapp.utils.getOrAwaitValue
import com.dicoding.storyapp.utils.DataDummy
import com.dicoding.storyapp.utils.DummyGlobal
import com.dicoding.storyapp.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddStoryActivityTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var userPreference: UserPreference
    private val dummyMain = DataDummy.generateDummyMain()
    private val dummyGlobal = DummyGlobal.generateDummyGlobal()

    @Before
    fun setup() {
        userPreference = Mockito.mock(UserPreference::class.java)
        addStoryViewModel = AddStoryViewModel(storiesRepository, userPreference)

    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when add story and return data`() {
        val file: RequestBody =
            "app/src/main/res/drawable/image_signup.png".toRequestBody("image/jpeg".toMediaTypeOrNull())
        val description = "Test".toRequestBody("text/plain".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "image_signup.png",
            file
        )
        val data: GlobalResponse = dummyGlobal
        val expectedData = MutableLiveData<GlobalResponse>()
        expectedData.value = data
        Mockito.`when`(
            storiesRepository.postStory(
                dummyMain.token,
                imageMultipart,
                description,
                1.00f,
                2.00f
            )
        ).thenReturn(expectedData)

        val actualData: GlobalResponse =
            addStoryViewModel.addStory(dummyMain.token, imageMultipart, description, 1.00f, 2.00f)
                .getOrAwaitValue()

        Assert.assertNotNull(actualData)
    }
}