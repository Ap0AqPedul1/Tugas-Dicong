package com.example.tugas_1_dicoding.adapeter

import androidx.recyclerview.widget.RecyclerView
import com.example.tugas_1_dicoding.dataClass.Item
import com.example.tugas_1_dicoding.databinding.ItemViewBinding

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    private val items = mutableListOf<Item>()

    fun addItems(newItems: List<Item>) {
        val startPos = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPos, newItems.size)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemViewBinding.inflate(android.view.LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ItemViewHolder(private val binding: ItemViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.textTitle.text = item.title
        }
    }
}
