package com.example.edupulse.presentation.ui.home.tasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.databinding.ItemTasksHomeBinding
import com.example.nasizae_edu_pulse.domain.model.TasksItemModel
import kotlin.random.Random

class TasksAdapter(private val list: ArrayList<TasksItemModel>,private val onItemClick:(possition:Int,experieceCount:Int)->Unit) : Adapter<TasksAdapter.TasksHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksHolder {
        return TasksHolder(
            ItemTasksHomeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: TasksHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class TasksHolder(private val binding: ItemTasksHomeBinding) : ViewHolder(binding.root) {
        fun bind(item: TasksItemModel) {
            val countExperience= 25
            binding.btnTasks.text=item.id.toString()
            binding.btnTasks.setOnClickListener {
             onItemClick(adapterPosition,countExperience)
            }
            if (item.unClocked) {
                binding.btnTasks.isEnabled = true
                binding.btnTasks.setBackgroundColor(binding.root.context.getColor(R.color.green))
                binding.btnTasks.setOnClickListener {
                    onItemClick(adapterPosition, countExperience)
                }
            } else {
                binding.btnTasks.isEnabled = false
                binding.btnTasks.setBackgroundColor(binding.root.context.getColor(R.color.grey))
            }
            if (adapterPosition % 2 == 1) {
                binding.containerBtn.translationX= 260F
            } else {
                binding.containerBtn.translationX = -160f
            }
        }
    }
}