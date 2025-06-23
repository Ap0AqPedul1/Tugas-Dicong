package com.example.tugas_1_dicoding.adapeter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_1_dicoding.dataClass.Story
import com.example.tugas_1_dicoding.databinding.ItemViewBinding
import com.bumptech.glide.Glide

class StoryAdapter(
    private val onItemClick: (Story) -> Unit
) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {
    private val items = mutableListOf<Story>()

    fun addItems(newItems: List<Story>) {
        val startPos = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPos, newItems.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = items[position]
        holder.bind(story)
        holder.itemView.setOnClickListener {
            onItemClick(story)
        }
    }

    inner class StoryViewHolder(private val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(story: Story) {
            binding.textTitle.text = story.description
            binding.textName.text = story.name
            Glide.with(binding.root.context)
                .load(story.photoUrl)
                .into(binding.imagePost)
        }
    }
}
