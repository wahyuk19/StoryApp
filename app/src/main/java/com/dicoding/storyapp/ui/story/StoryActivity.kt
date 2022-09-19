package com.dicoding.storyapp.ui.story

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.data.viewmodel.ViewModelFactory
import com.dicoding.storyapp.databinding.ActivityStoryBinding
import com.dicoding.storyapp.ui.addstory.AddStoryActivity
import com.dicoding.storyapp.ui.utils.messageFailed
import com.dicoding.storyapp.ui.utils.messageLoading

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var storyViewModel: StoryViewModel
    private var page: Int? = null
    private val size: Int? = null
    private val location = 0
    private lateinit var token: String
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
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
        dialog = Dialog(this)
    }

    private fun setupViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        storyViewModel = ViewModelProvider(
            this, factory
        )[StoryViewModel::class.java]

    }

    private fun setupAction() {
        val storyAdapter = StoryAdapter()

        storyViewModel.getUser(UserPreference.getInstance(dataStore)).observe(this) { user ->
            messageLoading(this, getString(R.string.loading), dialog)
            token = user.token
            storyViewModel.getStories("Bearer $token", page, size, location)
                .observe(this) { story ->
                    if (story.error == false) {
                        dialog.dismiss()
                        storyAdapter.setData(story.listStory)
                    } else {
                        dialog.dismiss()
                        messageFailed(this, story.message, dialog)
                        Handler(Looper.getMainLooper()).postDelayed({
                            dialog.dismiss()
                        }, 2000)
                    }
                }

        }

        with(binding.rvStory) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = storyAdapter
        }

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    messageLoading(this, getString(R.string.loading), dialog)
                    storyViewModel.getStories("Bearer $token", page, size, location)
                        .observe(this) { story ->
                            if (story.error == false) {
                                dialog.dismiss()
                                storyAdapter.setData(story.listStory)
                            } else {
                                dialog.dismiss()
                                messageFailed(this, story.message, dialog)
                                Handler(Looper.getMainLooper()).postDelayed({
                                    dialog.dismiss()
                                }, 2000)
                            }
                        }

                }
            }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

}