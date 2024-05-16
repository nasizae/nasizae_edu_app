package com.example.edupulse.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.edupulse.presentation.ui.home.adapter.HomeAdapter
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHomeView()

    }

    private fun initHomeView() {
        val tabLayout=binding.tabHome
        val viewPager=binding.vpHome
        val adapter= HomeAdapter(requireActivity().supportFragmentManager,lifecycle)
        viewPager.adapter=adapter
        TabLayoutMediator(tabLayout,viewPager){tab,possition->

            when(possition){
                0->tab.text="Задание"
                1->tab.text="Библиотека"
            }
        }.attach()
        // Установка цветов текста для активного и неактивного состояний
        tabLayout.setTabTextColors(
            ContextCompat.getColor(requireContext(), R.color.black),
            ContextCompat.getColor(requireContext(),R.color.baff))

        // Установка цвета подсветки выбранного таба
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(requireContext(),R.color.baff))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}