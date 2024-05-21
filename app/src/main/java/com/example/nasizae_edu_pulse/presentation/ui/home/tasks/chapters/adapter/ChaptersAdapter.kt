package com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.nasizae_edu_pulse.databinding.ItemChaptersBinding

class ChaptersAdapter(
    private val listChaptersName: ArrayList<String>,
    private val listCourse: ArrayList<String>,
    private val onIndividualClick: () -> Unit
) :
    Adapter<ChaptersAdapter.ChaptersHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChaptersHolder {
        return ChaptersHolder(
            ItemChaptersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = listCourse.size

    override fun onBindViewHolder(holder: ChaptersHolder, position: Int) {
        holder.bind(listChaptersName[position], listCourse[position])
    }

    inner class ChaptersHolder(val binding: ItemChaptersBinding) :
        ViewHolder(binding.root) {
        fun bind(chaptersName: String, course: String) {
            binding.tvChapterName.text = chaptersName
            binding.course.text = course
            binding.containerIndividual.isVisible = adapterPosition == listChaptersName.lastIndex
            binding.containerIndividual.setOnClickListener {
                onIndividualClick()
            }
        }

    }
}