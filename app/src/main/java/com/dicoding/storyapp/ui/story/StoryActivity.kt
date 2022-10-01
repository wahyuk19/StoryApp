package com.dicoding.storyapp.ui.story

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.model.UserPreference
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.data.viewmodel.ViewModelFactory
import com.dicoding.storyapp.databinding.ActivityStoryBinding
import com.dicoding.storyapp.ui.addstory.AddStoryActivity
import com.dicoding.storyapp.ui.utils.messageFailed
import com.dicoding.storyapp.ui.utils.messageLoading

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class StoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryBinding
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var token: String
    private lateinit var dialog: Dialog
    private val delayedTIme = 2000L
    private val storyAdapter = StoryAdapter()


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
        val factory = ViewModelFactory.getInstance(UserPreference.getInstance(dataStore))
        storyViewModel = ViewModelProvider(
            this, factory
        )[StoryViewModel::class.java]

    }

    private fun setupAction() {
        storyViewModel.getUser().observe(this) { user ->
            messageLoading(getString(R.string.loading), dialog)
            token = user.token
            storyViewModel.getStories("Bearer $token")
                .observe(this@StoryActivity) { story ->
                    observeStories(story, storyAdapter)
                }
        }

        with(binding.rvStory) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = storyAdapter
        }

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            resultLauncher.launch(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        storyAdapter.refresh()
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                messageLoading(getString(R.string.loading), dialog)
//                        storyViewModel.getStories("Bearer $token")
//                            .observe(this@StoryActivity) { story ->
                Log.e("upload", "upload data")
                storyAdapter.refresh()
//                            }
            }
        }

    private fun observeStories(story: PagingData<ListStoryItem>?, storyAdapter: StoryAdapter) {
        if (story != null) {
            dialog.dismiss()
            storyAdapter.submitData(lifecycle, story)
        } else {
            dialog.dismiss()
            messageFailed(getString(R.string.load_failed_story), dialog)
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
            }, delayedTIme)
        }
    }

}