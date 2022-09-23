package com.dicoding.storyapp.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.databinding.ActivityDetailBinding
import com.dicoding.storyapp.ui.utils.withDateFormat

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
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

    private fun setupAction() {
        val story = intent.getParcelableExtra<ListStoryItem>("detail") as ListStoryItem
        with(binding) {
            Glide.with(this@DetailActivity)
                .load(story.photoUrl)
                .apply(
                    RequestOptions.placeholderOf(R.drawable.ic_loading).override(100, 150)
                        .error(R.drawable.ic_error)
                )
                .into(ivDetailPhoto)
            tvDetailName.text = getString(R.string.created_by, story.name)
            tvDetailDate.text = story.createdAt?.withDateFormat()
            tvDetailDescription.text = story.description
        }
    }

}