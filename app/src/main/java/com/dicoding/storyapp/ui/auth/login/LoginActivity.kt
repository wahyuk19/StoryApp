package com.dicoding.storyapp.ui.auth.login

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.ui.utils.Constants
import com.dicoding.storyapp.ui.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private var emailStatus = false
    private var passwordStatus = false
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            Log.e("clicked","test")
//            if(!email.matches(emailPattern)) {
//                binding.edRegisterEmail.requestFocus()
//                binding.edRegisterEmail.error = getString(R.string.error_email)
//            }
//            if(password.length < 6){
//                if(email.matches(emailPattern)){
//                    binding.edRegisterPassword.requestFocus()
//                }else{
//                    binding.edRegisterEmail.requestFocus()
//                }
//                binding.edRegisterPassword.setError(getString(R.string.error_password),null)
//            }
        }
    }

    private fun controllerEditText(textInput: EditText,type: Int) {
        textInput.addTextChangedListener {
            when(type){
                1 -> {
                    emailStatus = Constants.EMAIL_STATUS
                }
                2 -> {
                    passwordStatus = Constants.PASSWORD_STATUS
                }
            }

            binding.loginButton.isEnabled = emailStatus && passwordStatus
        }
//        val isEmptyValue = textInput.text?.isEmpty()
//        binding.signupButton.isEnabled = isEmptyValue != true
//        if(binding.signupButton.isEnabled){
//            Log.e("status","clicked")
//        }
    }

    private fun setupViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]
    }

    private fun setupView() {
        binding.loginButton.isEnabled = emailStatus && passwordStatus
        controllerEditText(binding.edLoginEmail,1)
        controllerEditText(binding.edLoginPassword,2)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}