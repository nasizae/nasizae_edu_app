package com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.nasizae_edu_pulse.databinding.ItemChaptersBinding

class ChaptersAdapter(private val onIndividualClick:()->Unit): Adapter<ChaptersAdapter.ChaptersHolder>() {
    private val list =
        mutableListOf<String>("hfjgdfhgdsfhjd", "fsjhgfdshgvf", "dfgsjfhgdjgshj", "fkdsfgdjgfs","fbhdsfjs","dfjsbgfjdsgj")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChaptersHolder {
        return ChaptersHolder(
            ItemChaptersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ChaptersHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ChaptersHolder(private val binding: ItemChaptersBinding) :
        ViewHolder(binding.root) {
        fun bind(chaptersName: String) {
            binding.tvTitleTasks.text = chaptersName
            binding.containerIndividual.isVisible = adapterPosition == list.lastIndex
            binding.containerIndividual.setOnClickListener {
                onIndividualClick()
            }
        }

    }
}