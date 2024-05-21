package com.example.nasizae_edu_pulse.presentation.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.edupulse.presentation.ui.home.adapter.HomeAdapter
import com.example.nasizae_edu_pulse.R
import com.google.android.material.tabs.TabLayoutMediator

class HomeScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHomeView()

    }

    private fun initHomeView() {
        val tabLayout = binding.tabHome
        val viewPager = binding.vpHome
        val adapter = HomeAdapter(requireActivity().supportFragmentManager, lifecycle)
        viewPager.adapter = adapter
        val data=arguments?.getString("key")
        val bundle = Bundle()
        bundle.putString("key", data)
        adapter.setArguments(bundle)
        TabLayoutMediator(tabLayout, viewPager) { tab, possition ->
            when (possition) {
                0 -> tab.text = "Задание"
                1 -> tab.text = "Библиотека"
            }
        }.attach()
        tabLayout.setTabTextColors(
            ContextCompat.getColor(requireContext(), R.color.black),
            ContextCompat.getColor(requireContext(), R.color.baff)
        )

        tabLayout.setSelectedTabIndicatorColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.baff
            )
        )
    }
}