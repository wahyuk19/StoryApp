package com.dicoding.storyapp.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import androidx.paging.AsyncPagingDataDiffer
import com.dicoding.storyapp.data.model.LoginRequest
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import com.dicoding.storyapp.utils.getOrAwaitValue
import com.dicoding.storyapp.ui.story.StoryAdapter
import com.dicoding.storyapp.ui.story.noopListUpdateCallback
import com.dicoding.storyapp.utils.DataDummy
import com.dicoding.storyapp.utils.DummyStories
import com.dicoding.storyapp.utils.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class StoriesRepositoryTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var remote: RemoteDataSource
    private lateinit var storyRepository: FakeStoriesRepository

    @Before
    fun setUp() {
        remote = Mockito.mock(RemoteDataSource::class.java)
        storyRepository = FakeStoriesRepository()

    }

    @Test
    fun `when register should not null`() = runTest {
        val dummy = DataDummy.generateDummyMain()
        val request = RegisterRequest(dummy.name, dummy.email, dummy.password)
        val actual = storyRepository.postRegister(request)
        assertNotNull(actual)
    }

    @Test
    fun `when login should not null`() = runTest {
        val dummy = DataDummy.generateDummyMain()
        val request = LoginRequest(dummy.email, dummy.password)
        val actual = storyRepository.postLogin(request)
        assertNotNull(actual)
    }

    @Test
    fun `when post story should not null`() = runTest {
        val file: RequestBody =
            "app/src/main/res/drawable/image_signup.png".toRequestBody("image/jpeg".toMediaTypeOrNull())
        val description = "Test".toRequestBody("text/plain".toMediaType())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "image_signup.png",
            file
        )
        val dummy = DataDummy.generateDummyMain()
        val actual = storyRepository.postStory(dummy.token, imageMultipart, description, null, null)
        assertNotNull(actual)
    }

    @Test
    fun `when get story by map should not null`() = runTest {
        val dummy = DataDummy.generateDummyMain()
        val dummyStories = liveData<StoriesResponse> { DummyStories.generateDummyStories() }
        val actual = storyRepository.getStoriesByMap(dummy.token, 1)
        assertNotNull(actual)
        assertEquals(dummyStories.value?.listStory, actual.value?.listStory)
    }

    @Test
    fun `when get story should not null`() = runTest {
        val dummy = DataDummy.generateDummyMain()
        val dummyStories = DummyStories.generateDummyStories()
        val actual = storyRepository.getStories(dummy.token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actual)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.listStory, differ.snapshot())
        assertEquals(dummyStories.listStory.size, differ.snapshot().size)
    }

}