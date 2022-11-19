package com.techblue.virginmoney.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.techblue.virginmoney.R
import com.techblue.virginmoney.databinding.PersonItemBinding
import com.techblue.virginmoney.listeners.PersonItemClickListener
import com.techblue.virginmoney.models.Person

class PersonsPagingAdapter constructor(private val personItemClickListener: PersonItemClickListener) : PagingDataAdapter<Person, PersonsPagingAdapter.PersonViewHolder>(MovieComparator) {

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person: Person = getItem(position)!!

        holder.itemView.setOnClickListener {
            personItemClickListener.onPersonItemClickListener(person)
        }

        holder.view.personNameTxt.text = "${person.firstName} ${person.lastName}"
        holder.view.jobTitleTxt.text = "${person.jobtitle}"
        if (!person.avatar.isNullOrEmpty())
            Glide.with(holder.view.avatarImg.context).load(person.avatar).placeholder(R.drawable.ic_baseline_broken_image_24).into(holder.view.avatarImg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PersonItemBinding.inflate(inflater, parent, false)
        return PersonViewHolder(binding)
    }

    class PersonViewHolder(val view: PersonItemBinding) : RecyclerView.ViewHolder(view.root) {

    }

    object MovieComparator : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
            return oldItem == newItem
        }
    }
}