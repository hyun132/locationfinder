package com.example.locationfinder.ui.map.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.locationfinder.databinding.SearchItemBinding
import com.example.locationfinder.models.bridge.BrMcLocationItem

/**
 * LocationRecyclerviewAdapter
 */
class SearchRecyclerviewAdapter :
    PagingDataAdapter<BrMcLocationItem, SearchRecyclerviewAdapter.SearchItemViewHolder>(differCallback) {
    companion object {
        val differCallback = object : DiffUtil.ItemCallback<BrMcLocationItem>() {
            override fun areItemsTheSame(oldLocationItem: BrMcLocationItem, newLocationItem: BrMcLocationItem): Boolean {
                return oldLocationItem.id == newLocationItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldLocationItem: BrMcLocationItem, newLocationItem: BrMcLocationItem): Boolean {
                return oldLocationItem == newLocationItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchItemViewHolder {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SearchItemViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                if (item != null) {
                    it(item)
                }
            }
        }
        if (item != null) {
            holder.bind(item)
        }
    }

    class SearchItemViewHolder(private var binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(mcLocationItem: BrMcLocationItem) {
            binding.tvAddress.text = mcLocationItem.roadAddressName
            binding.tvCategory.text = mcLocationItem.categoryGroupName
            binding.tvDistance.text = "${mcLocationItem.distance}"
            binding.tvTitle.text = mcLocationItem.placeName
            binding.executePendingBindings()
        }
    }

    private var onItemClickListener: ((BrMcLocationItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (BrMcLocationItem) -> Unit) {
        onItemClickListener = listener
    }
}