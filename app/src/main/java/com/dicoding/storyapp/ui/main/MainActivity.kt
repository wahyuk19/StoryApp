package com.dicoding.storyapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.data.viewmodel.ViewModelFactory
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.ui.auth.welcome.WelcomeActivity
import com.dicoding.storyapp.ui.story.StoryActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        mainViewModel = ViewModelProvider(
            this, factory
        )[MainViewModel::class.java]

        mainViewModel.getUser(UserPreference.getInstance(dataStore)).observe(this) { user ->
            if (user.isLogin) {
                binding.nameTextView.text = getString(R.string.greeting, user.name)
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
    }

    private fun setupAction() {
        binding.actionStory.setOnClickListener {
            startActivity(Intent(this, StoryActivity::class.java))
        }

        binding.actionSetting.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.actionLogout.setOnClickListener {
            mainViewModel.logout(UserPreference.getInstance(dataStore))
        }
    }
}