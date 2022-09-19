package com.dicoding.storyapp.ui.story

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.remote.response.ListStoryItem
import com.dicoding.storyapp.databinding.ItemStoryBinding
import com.dicoding.storyapp.ui.detail.DetailActivity
import com.dicoding.storyapp.ui.utils.withDateFormat


class StoryAdapter : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private var listData = ArrayList<ListStoryItem>()

    fun setData(newListData: List<ListStoryItem>?) {
        if (newListData == null) return
        val diffCallback = StoryDiffCallback(listData, newListData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        listData.clear()
        listData.addAll(newListData)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val itemsStoryBinding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(itemsStoryBinding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val movie = listData[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int =
        listData.size

    inner class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(story: ListStoryItem) {
            with(binding) {
                tvItemName.text = story.name
                tvItemDate.text = story.createdAt?.withDateFormat()
                Glide.with(itemView.context)
                    .load(story.photoUrl)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading).override(100, 150)
                            .error(R.drawable.ic_error)
                    )
                    .into(ivItemPhoto)

                root.setOnClickListener {
                    val intent = Intent(root.context, DetailActivity::class.java)
                    intent.putExtra("detail", story)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            root.context as Activity,
                            Pair(ivItemPhoto, "photo"),
                            Pair(tvItemName, "name"),
                            Pair(tvItemDate, "date")
                        )
                    root.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }

    }

}