package com.example.edupulse.presentation.ui.home.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.nasizae_edu_pulse.databinding.ItemLibraryBinding

class LibraryAdapter : Adapter<LibraryAdapter.LibraryHolder>() {

    private val list =
        mutableListOf("Изучение Андроид разработки", "Язык програмирование Java", "JetPack compose")

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
            binding.tvNameLibrary.text = item
        }
    }
}