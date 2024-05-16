package com.example.edupulse.presentation.ui.home.tasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.nasizae_edu_pulse.databinding.ItemTasksHomeBinding

class TasksAdapter(private val list: ArrayList<Int>,private val onItemClick:(possition:Int)->Unit) : Adapter<TasksAdapter.TasksHolder>() {
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
        fun bind(item: Int) {
            binding.btnTasks.setImageResource(item)
            binding.btnTasks.setOnClickListener {
             onItemClick(adapterPosition)
            }
            if (adapterPosition % 2 == 1) {
                binding.btnTasks.translationX= 260F
            } else {
                binding.btnTasks.translationX = -160f
            }
        }
    }
}