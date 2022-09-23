package com.dicoding.storyapp.ui.auth.signup

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.BuildConfig
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.data.viewmodel.ViewModelFactory
import com.dicoding.storyapp.databinding.ActivitySignUpBinding
import com.dicoding.storyapp.ui.utils.*

class SignUpActivity : AppCompatActivity() {
    private var nameStatus = false
    private var emailStatus = false
    private var passwordStatus = false
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var dialog: Dialog
    private val delayedTIme = 2000L

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
            messageLoading(getString(R.string.loading), dialog)
            val name = binding.edRegisterName.text.toString()
            val email = binding.edRegisterEmail.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            val tripleDES = TripleDES(BuildConfig.KEY)
            val encPass = tripleDES.encryptPKCS5(password)
            val encPassHex = encPass.toHex()
            val register = RegisterRequest(name, email, encPassHex)
            signUpViewModel.register(register).observe(this) {
                if (it.error == false) {
                    dialog.dismiss()
                    messageSuccess(getString(R.string.success_reg), dialog)
                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
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
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        signUpViewModel = ViewModelProvider(
            this, factory
        )[SignUpViewModel::class.java]
    }

    private fun setupView() {
        binding.signupButton.isEnabled = nameStatus && emailStatus && passwordStatus
        controllerEditText(binding.edRegisterName, 1)
        controllerEditText(binding.edRegisterEmail, 2)
        controllerEditText(binding.edRegisterPassword, 3)
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

    fun ByteArray.toHex(): String =
        joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

}