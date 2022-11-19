package com.techblue.virginmoney.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.techblue.virginmoney.databinding.RoomItemBinding
import com.techblue.virginmoney.models.Room

class RoomsPagingAdapter : PagingDataAdapter<Room, RoomsPagingAdapter.RoomViewHolder>(MovieComparator) {

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room: Room = getItem(position)!!
        holder.view.roomOccupiedTxt.text = if (room.isOccupied) "Occupied" else "Available"
        holder.view.roomOccupiedTxt.setTextColor(if (room.isOccupied) Color.RED else Color.GREEN)

        holder.view.roomCapacityTxt.text = "Capacity: ${room.maxOccupancy}"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RoomItemBinding.inflate(inflater, parent, false)
        return RoomViewHolder(binding)
    }

    class RoomViewHolder(val view: RoomItemBinding) : RecyclerView.ViewHolder(view.root) {

    }

    object MovieComparator : DiffUtil.ItemCallback<Room>() {
        override fun areItemsTheSame(oldItem: Room, newItem: Room): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Room, newItem: Room): Boolean {
            return oldItem == newItem
        }
    }
}