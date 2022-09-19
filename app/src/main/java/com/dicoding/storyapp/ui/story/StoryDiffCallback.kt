package com.dicoding.storyapp.ui.story

import androidx.recyclerview.widget.DiffUtil
import com.dicoding.storyapp.data.remote.response.ListStoryItem

class StoryDiffCallback(
    private val oldList: List<ListStoryItem>,
    private val newList: List<ListStoryItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldStoryList = oldList[oldItemPosition]
        val newStoryList = newList[newItemPosition]

        return oldStoryList.id.equals(newStoryList.id)
    }

}