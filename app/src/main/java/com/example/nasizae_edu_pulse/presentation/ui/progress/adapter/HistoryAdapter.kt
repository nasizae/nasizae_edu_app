package com.example.nasizae_edu_pulse.presentation.ui.progress.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.nasizae_edu_pulse.databinding.ItemHistoryBinding
import com.example.nasizae_edu_pulse.domain.model.userResult

class HistoryAdapter : Adapter<HistoryAdapter.HystoryHolder>() {
    private val list = mutableListOf<userResult>()
    fun addData(data: List<userResult>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HystoryHolder {
        return HystoryHolder(
            ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: HystoryHolder, position: Int) {
        holder.bind(list[position])
    }

    class HystoryHolder(private val binding: ItemHistoryBinding) : ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(userResult: userResult) {
            binding.time.text = userResult.time.toString()
            binding.tvCountRightAnswers.text = userResult.total.toString()+"%"
            binding.tvTasks.text = "Задание ${(userResult.tasksNumber!! + 1)}"
        }
    }
}