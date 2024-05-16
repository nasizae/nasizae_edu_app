package com.example.edupulse.presentation.ui.onBoarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.edupulse.data.pref.Pref
import com.example.edupulse.presentation.ui.onBoarding.adapter.OnBoardingAdapter
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {
    private lateinit var binding: FragmentOnBoardingBinding
    private lateinit var adapter: OnBoardingAdapter
    private val pref:Pref by lazy {
        Pref(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentOnBoardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter= OnBoardingAdapter (this::OnCLick)
        binding.viewPager.adapter=adapter
        binding.indicator.setViewPager(binding.viewPager)
    }

    private fun OnCLick() {
        pref.onOnBoardingShowed()
        findNavController().navigate(R.id.authenticationFragment)
    }

}