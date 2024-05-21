package com.example.nasizae_edu_pulse.presentation.ui.home.tasks.awards

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.edupulse.data.model.Users
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.databinding.FragmentAwardsBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AwardsFragment : Fragment() {
    private val firestoreDb=FirebaseFirestore.getInstance()
    private lateinit var binding:FragmentAwardsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentAwardsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
                    binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        initData()

    }

    private fun initData() {
        val data = firestoreDb.collection("chapters").document("chpater").get()
        data.addOnSuccessListener { document ->
            val list=ArrayList<String>()
            document.data?.values?.joinToString()?.let { list.add(it) }
            list.forEach {
                binding.tvTitle.text=it
            }
        }
    }


}