package com.example.nasizae_edu_pulse.presentation.ui.chat.theme_message.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.nasizae_edu_pulse.data.model.ThemeMessageModel
import com.example.nasizae_edu_pulse.databinding.ItemThemeMessageBinding

class ThemeAdapter (private val onItemClick:(String)->Unit): Adapter<ThemeAdapter.ThemeHolder>() {
    private val list = mutableListOf<ThemeMessageModel>()

    fun addData(listData:List<ThemeMessageModel>){
       list.clear()
       list.addAll(listData)
       notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeHolder {
        return ThemeHolder(
            ItemThemeMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ThemeHolder, position: Int) {
        holder.bind(list.get(position))
    }

    inner class ThemeHolder(private val binding: ItemThemeMessageBinding) :
        ViewHolder(binding.root) {
        fun bind(themeMessageModel: ThemeMessageModel) {
            binding.tvThemeMessage.text = themeMessageModel.userThemeMessage
            Glide.with(binding.imgUser).load(themeMessageModel.userImage).into(binding.imgUser)
            itemView.setOnClickListener {
                themeMessageModel.key.let { onItemClick(it.toString()) }
            }
        }
    }
}