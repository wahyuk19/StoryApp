package com.dicoding.storyapp.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.data.remote.StoriesRepository
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import com.dicoding.storyapp.utils.getOrAwaitValue
import com.dicoding.storyapp.utils.DataDummy
import com.dicoding.storyapp.utils.DummyStories
import com.dicoding.storyapp.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class MapsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var userPreference: UserPreference
    private val dummyMain = DataDummy.generateDummyMain()
    private val dummyStories = DummyStories.generateDummyStories()

    @Before
    fun setup() {
        userPreference = Mockito.mock(UserPreference::class.java)
        mapsViewModel = MapsViewModel(storiesRepository, userPreference)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when get stories by map request and return data`() {
        val data: StoriesResponse = dummyStories
        val expectedData = MutableLiveData<StoriesResponse>()
        expectedData.value = data
        Mockito.`when`(storiesRepository.getStoriesByMap(dummyMain.token, 1))
            .thenReturn(expectedData)

        val actualData: StoriesResponse =
            mapsViewModel.getStories(dummyMain.token, 1).getOrAwaitValue()

        Assert.assertNotNull(actualData.listStory)
        Assert.assertEquals(dummyStories.listStory, actualData.listStory)
        Assert.assertEquals(dummyStories.listStory.size, actualData.listStory.size)
    }
}