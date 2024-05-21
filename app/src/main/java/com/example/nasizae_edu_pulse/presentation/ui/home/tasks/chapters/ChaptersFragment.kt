package com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.data.model.ChapterModel
import com.example.nasizae_edu_pulse.databinding.FragmentChaptersBinding
import com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters.adapter.ChaptersAdapter
import com.google.firebase.firestore.FirebaseFirestore

class ChaptersFragment : Fragment() {
    private lateinit var binding: FragmentChaptersBinding
    private lateinit var adapter: ChaptersAdapter
    private val data = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChaptersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataChapters = data.collection("chapters").document("chpater").get()
        dataChapters.addOnSuccessListener {
            if (it != null && it.exists()) {
                val chapter = it.toObject(ChapterModel::class.java)
                if (chapter != null) {
                    adapter = ChaptersAdapter(
                        chapter.chaptersName ?: arrayListOf(),
                        chapter.course ?: arrayListOf(),
                        this::onIndividualCLick
                    )
                    binding.rvChapters.adapter = adapter
                }
            }
        }.addOnFailureListener {
            Log.d("ololo", "Error getting document: ", it)
        }


    }

    private fun onIndividualCLick() {
        findNavController().navigate(R.id.individualTasksFragment)
    }
}
