package com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.databinding.FragmentChaptersBinding
import com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters.adapter.ChaptersAdapter

class ChaptersFragment : Fragment() {
    private lateinit var binding:FragmentChaptersBinding
    private val adapter=ChaptersAdapter(this::onIndividualCLick)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentChaptersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvChapters.adapter=adapter
    }

    private fun onIndividualCLick() {
    findNavController().navigate(R.id.individualTasksFragment)
    }
}
