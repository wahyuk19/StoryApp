package com.dicoding.storyapp.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.data.remote.StoriesRepository
import com.dicoding.storyapp.data.remote.response.GlobalResponse
import com.dicoding.storyapp.data.remote.response.StoriesResponse
import com.dicoding.storyapp.getOrAwaitValue
import com.dicoding.storyapp.ui.auth.signup.SignUpViewModel
import com.dicoding.storyapp.utils.DataDummy
import com.dicoding.storyapp.utils.DummyGlobal
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
class StoryViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var userPreference: UserPreference
    private val dummyMain = DataDummy.generateDummyMain()
    private val dummyGlobal = DummyGlobal.generateDummyGlobal()

    @Before
    fun setup(){
        userPreference = Mockito.mock(UserPreference::class.java)
        storyViewModel = StoryViewModel(storiesRepository,userPreference)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when get stories and return data`() {
        val expectedData = MutableLiveData<StoriesResponse>()
        expectedData.value = dummyGlobal
        Mockito.`when`(storiesRepository.getStories(dummyMain.token)).thenReturn(expectedData)

        val actualGetStories = storyViewModel.getStories(dummyMain.token).getOrAwaitValue()
        Mockito.verify(storiesRepository).getStories(dummyMain.token)
        Assert.assertNotNull(actualGetStories)
    }
}