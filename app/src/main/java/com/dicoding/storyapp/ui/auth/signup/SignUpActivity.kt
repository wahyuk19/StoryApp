package com.dicoding.storyapp.ui.auth.signup

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
import com.dicoding.storyapp.databinding.ActivitySignUpBinding
import com.dicoding.storyapp.ui.utils.Constants
import com.dicoding.storyapp.ui.viewmodel.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SignUpActivity : AppCompatActivity() {
    private var nameStatus = false
    private var emailStatus = false
    private var passwordStatus = false
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
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
                    nameStatus = Constants.NAME_STATUS
                }
                2 -> {
                    emailStatus = Constants.EMAIL_STATUS
                }
                3 -> {
                    passwordStatus = Constants.PASSWORD_STATUS
                }
            }

            binding.signupButton.isEnabled = nameStatus && emailStatus && passwordStatus
        }
//        val isEmptyValue = textInput.text?.isEmpty()
//        binding.signupButton.isEnabled = isEmptyValue != true
//        if(binding.signupButton.isEnabled){
//            Log.e("status","clicked")
//        }
    }

    private fun setupViewModel() {
        signUpViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[SignUpViewModel::class.java]
    }

    private fun setupView() {
        binding.signupButton.isEnabled = nameStatus && emailStatus && passwordStatus
        controllerEditText(binding.edRegisterName,1)
        controllerEditText(binding.edRegisterEmail,2)
        controllerEditText(binding.edRegisterPassword,3)
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