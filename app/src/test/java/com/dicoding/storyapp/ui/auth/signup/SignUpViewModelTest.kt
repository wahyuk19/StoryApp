package com.dicoding.storyapp.ui.auth.signup

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.data.remote.StoriesRepository
import com.dicoding.storyapp.data.remote.response.GlobalResponse
import com.dicoding.storyapp.utils.getOrAwaitValue
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
class SignUpViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var signUpViewModel: SignUpViewModel
    private val dummyMain = DataDummy.generateDummyMain()
    private val dummyGlobal = DummyGlobal.generateDummyGlobal()

    @Before
    fun setup() {
        signUpViewModel = SignUpViewModel(storiesRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when send register request and return data`() {
        val expectedData = MutableLiveData<GlobalResponse>()
        expectedData.value = dummyGlobal
        val register = RegisterRequest(dummyMain.name, dummyMain.email, dummyMain.password)
        Mockito.`when`(storiesRepository.postRegister(register)).thenReturn(expectedData)

        val actualRegister = signUpViewModel.register(register).getOrAwaitValue()
        Mockito.verify(storiesRepository).postRegister(register)
        Assert.assertNotNull(actualRegister)
    }
}
