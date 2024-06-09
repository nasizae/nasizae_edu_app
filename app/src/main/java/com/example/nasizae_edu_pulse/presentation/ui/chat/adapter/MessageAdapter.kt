package com.example.edupulse.presentation.ui.chat.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.edupulse.data.model.UserMessageModel
import com.example.nasizae_edu_pulse.databinding.ItemMessageBinding

class MessageAdapter(private val list:ArrayList<UserMessageModel>) : Adapter<MessageAdapter.MessageHolder>() {

//    fun addData(userMessageModel: List<UserMessageModel>){
//        list.clear()
//        list.addAll(userMessageModel)
//        notifyItemRangeChanged(list.size,userMessageModel.size - list.size)
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        return MessageHolder(
            ItemMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.bind(list[position])
    }

    class MessageHolder(private val binding: ItemMessageBinding) : ViewHolder(binding.root) {
        fun bind(userMessageModel: UserMessageModel) {
            binding.tvMessage.text = userMessageModel.message
            binding.tvUsername.text = userMessageModel.username
            binding.tvDate.text=userMessageModel.date
            binding.tvTime.text=userMessageModel.time

            Glide.with(binding.imgUser).load(userMessageModel.imageUser).into(binding.imgUser)

        }
    }
}