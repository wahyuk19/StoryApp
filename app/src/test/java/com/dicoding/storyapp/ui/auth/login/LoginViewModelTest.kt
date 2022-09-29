package com.dicoding.storyapp.ui.auth.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.storyapp.data.model.LoginRequest
import com.dicoding.storyapp.data.model.UserModel
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.data.remote.StoriesRepository
import com.dicoding.storyapp.data.remote.response.GlobalResponse
import com.dicoding.storyapp.data.remote.response.LoginResponse
import com.dicoding.storyapp.getOrAwaitValue
import com.dicoding.storyapp.utils.DataDummy
import com.dicoding.storyapp.utils.DummyLogin
import com.dicoding.storyapp.utils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userPreference: UserPreference
    private val dummyMain = DataDummy.generateDummyMain()
    private val dummyLogin = DummyLogin.generateDummyLogin()

    @Before
    fun setup(){
        userPreference = mock(UserPreference::class.java)
        loginViewModel = LoginViewModel(storiesRepository,userPreference)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when user save session`() = runTest {
        val expectedSession = MutableLiveData<UserModel>()
        expectedSession.value = dummyMain
        lenient().`when`(userPreference.login(dummyMain)).thenReturn(true)
        loginViewModel.saveSession(dummyMain)
    }

    @Test
    fun `when send login request and return data`() {
        val expectedData = MutableLiveData<LoginResponse>()
        expectedData.value = dummyLogin
        val login = LoginRequest(dummyMain.email,dummyMain.password)
        `when`(storiesRepository.postLogin(login)).thenReturn(expectedData)

        val actualLogin = loginViewModel.login(login).getOrAwaitValue()
        verify(storiesRepository).postLogin(login)
        Assert.assertNotNull(actualLogin)
    }

}