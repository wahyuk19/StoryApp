package com.dicoding.storyapp.ui.main

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import com.dicoding.storyapp.data.model.UserModel
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.getOrAwaitValue
import com.dicoding.storyapp.utils.DataDummy
import com.dicoding.storyapp.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.Assert
import org.junit.Rule
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mainViewModel: MainViewModel
    private lateinit var userPreference: UserPreference
    private val dummyMain = DataDummy.generateDummyMain()

    @Before
    fun setup(){
        userPreference = mock(UserPreference::class.java)
        mainViewModel = MainViewModel(userPreference)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when user not null and return data`() = runTest {
        val observer = Observer<UserModel>{}
        try {
            val expectedUser = MutableLiveData<UserModel>()
            expectedUser.value = dummyMain
            `when`(userPreference.getUser()).thenReturn(expectedUser.asFlow())

            val actualUser = mainViewModel.getUser().observeForever(observer)

            verify(userPreference).getUser()
            Assert.assertNotNull(actualUser)
        }finally{
            mainViewModel.getUser().removeObserver(observer)
        }
    }

    @Test
    fun `when isLogin false and session end`() = runTest {
        val expectedBoolean = MutableLiveData<Boolean>()
        expectedBoolean.value = dummyMain.isLogin
        lenient().`when`(userPreference.isLogin()).thenReturn(expectedBoolean)
        mainViewModel.logout()
        Assert.assertTrue(dummyMain.isLogin)
    }
}