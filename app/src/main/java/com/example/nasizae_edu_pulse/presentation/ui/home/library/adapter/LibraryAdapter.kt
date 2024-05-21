package com.example.edupulse.presentation.ui.home.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.nasizae_edu_pulse.databinding.ItemLibraryBinding

class LibraryAdapter(private val list:List<String>,private val onItemClick:(position:Int)->Unit,private val onItemLongClick:(position:Int,libraryName:String)->Boolean) : Adapter<LibraryAdapter.LibraryHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryHolder {
        return LibraryHolder(
            ItemLibraryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: LibraryHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class LibraryHolder(private val binding: ItemLibraryBinding) : ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.tvLibraryName.text = item
            itemView.setOnClickListener {
                onItemClick(adapterPosition)
            }
            itemView.setOnLongClickListener {
                onItemLongClick(adapterPosition,item)
            }
        }
    }
}