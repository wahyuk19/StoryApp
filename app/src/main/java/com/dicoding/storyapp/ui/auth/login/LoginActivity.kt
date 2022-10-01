package com.dicoding.storyapp.ui.auth.login

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.BuildConfig
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.model.LoginRequest
import com.dicoding.storyapp.data.model.UserModel
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.data.viewmodel.ViewModelFactory
import com.dicoding.storyapp.databinding.ActivityLoginBinding
import com.dicoding.storyapp.ui.main.MainActivity
import com.dicoding.storyapp.ui.utils.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LoginActivity : AppCompatActivity() {
    private var emailStatus = false
    private var passwordStatus = false
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var dialog: Dialog
    private val delayedTIme = 2000L

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
            messageLoading(getString(R.string.loading), dialog)
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            val tripleDES = TripleDES(BuildConfig.KEY)
            val encPass = tripleDES.encryptPKCS5(password)
            val encPassHex = encPass.toHex()
            val login = LoginRequest(email, encPassHex)
            loginViewModel.login(login).observe(this) {
                if (it.error == false) {
                    dialog.dismiss()
                    messageSuccess(getString(R.string.success_login), dialog)
                    val name = it.loginResult?.name.toString()
                    val token = it.loginResult?.token.toString()
                    val session = UserModel(token, name, "", "", false)
                    loginViewModel.saveSession(session)
                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                        dialog.dismiss()
                    }, delayedTIme)
                } else {
                    dialog.dismiss()
                    messageFailed(it.message, dialog)
                    Handler(Looper.getMainLooper()).postDelayed({
                        dialog.dismiss()
                    }, delayedTIme)
                }

            }
        }
    }

    private fun controllerEditText(textInput: EditText, type: Int) {
        textInput.addTextChangedListener {
            when (type) {
                1 -> {
                    emailStatus = Constants.EMAIL_STATUS
                }
                2 -> {
                    passwordStatus = Constants.PASSWORD_STATUS
                }
            }

            binding.loginButton.isEnabled = emailStatus && passwordStatus
        }
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(UserPreference.getInstance(dataStore))
        loginViewModel = ViewModelProvider(
            this, factory
        )[LoginViewModel::class.java]
    }

    private fun setupView() {
        binding.loginButton.isEnabled = emailStatus && passwordStatus
        controllerEditText(binding.edLoginEmail, 1)
        controllerEditText(binding.edLoginPassword, 2)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
        dialog = Dialog(this)
    }

}