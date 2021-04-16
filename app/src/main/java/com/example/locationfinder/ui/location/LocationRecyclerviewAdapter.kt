package com.example.locationfinder.ui.location

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.locationfinder.R
import com.example.locationfinder.databinding.LocationItemBinding
import com.example.locationfinder.models.bridge.BrMcLocationItem

/**
 * LocationRecyclerviewAdapter
 */
class LocationRecyclerviewAdapter :
    PagedListAdapter<BrMcLocationItem, LocationRecyclerviewAdapter.LocationItemViewHolder>(
        differCallback
    ) {

    var onItemClickListener: ItemClickListener? = null

    fun setLocationItemClickListener(listener: ItemClickListener) {
        this.onItemClickListener = listener
    }

    /**
     * ItemClickListener
     */
    interface ItemClickListener {
        fun onFavoriteClicked(locationItem: BrMcLocationItem)
        fun onItemClicked(locationItem: BrMcLocationItem)
        fun onItemLongClicked(locationItem: BrMcLocationItem)
    }

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<BrMcLocationItem>() {
            override fun areItemsTheSame(
                oldLocationItem: BrMcLocationItem,
                newLocationItem: BrMcLocationItem
            ): Boolean {
                return (oldLocationItem.id == newLocationItem.id) && (oldLocationItem.favorite == newLocationItem.favorite)
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldLocationItem: BrMcLocationItem,
                newLocationItem: BrMcLocationItem
            ): Boolean {
                return oldLocationItem == newLocationItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationItemViewHolder {
        val binding =
            LocationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LocationItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LocationItemViewHolder,
        position: Int
    ) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class LocationItemViewHolder(private var binding: LocationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            this.itemView.setOnLongClickListener {
                getItem(this.absoluteAdapterPosition)?.let { item ->
                    onItemClickListener?.onItemLongClicked(item)
                }; false
            }

            this.itemView.setOnClickListener {
                getItem(this.absoluteAdapterPosition)?.let { item ->
                    onItemClickListener?.onItemClicked(item)
                }
            }
        }

        fun bind(brMcLocationItem: BrMcLocationItem) {
            binding.ivLiked.setOnClickListener {
                onItemClickListener?.onFavoriteClicked(brMcLocationItem)
            }

            binding.apply {
                ivLiked.setImageResource(if (brMcLocationItem.favorite) R.drawable.ic_yellow_star else R.drawable.ic_gray_star)
                tvAddress.text = brMcLocationItem.addressName
                tvCategory.text = brMcLocationItem.categoryGroupName
                tvTitle.text = brMcLocationItem.placeName
                executePendingBindings()
            }
        }
    }
}