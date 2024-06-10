package com.example.nasizae_edu_pulse.presentation.ui.personal_library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.nasizae_edu_pulse.data.model.PersonalLibraryModel
import com.example.nasizae_edu_pulse.databinding.ItemLibraryBinding

class PersonalLibraryAdapter(
    private val onItemClick: (numberLinks: Int) -> Unit
) : Adapter<PersonalLibraryAdapter.PersonalLibraryHolder>() {
    private var list: List<PersonalLibraryModel> = emptyList()
    fun updateData(personalLibraryModel: List<PersonalLibraryModel>){
        list=personalLibraryModel
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalLibraryHolder {
        return PersonalLibraryHolder(
            ItemLibraryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: PersonalLibraryHolder, position: Int) {
        holder.bind(list.get(position))
    }

    inner class PersonalLibraryHolder(private val binding: ItemLibraryBinding) :
        ViewHolder(binding.root) {
        fun bind(personalLibraryModel: PersonalLibraryModel) {
            binding.tvLibraryName.text = personalLibraryModel.libraryName
            itemView.setOnClickListener {
                if (personalLibraryModel.position != null) {
                    onItemClick(personalLibraryModel.position)
                }
            }
        }
    }
}