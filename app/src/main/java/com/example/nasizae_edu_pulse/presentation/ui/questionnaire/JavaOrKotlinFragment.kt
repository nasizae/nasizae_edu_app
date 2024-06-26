package com.example.nasizae_edu_pulse.presentation.ui.questionnaire

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.edupulse.data.pref.Pref
import com.example.edupulse.domain.usecase.RegistrationUseCase.Companion.USER
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.databinding.FragmentJavaOrKotlinBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database

class JavaOrKotlinFragment : Fragment() {
    private lateinit var binding: FragmentJavaOrKotlinBinding
    private val myDataBase = Firebase.database.getReference(USER)
    private val auth = FirebaseAuth.getInstance()
    private val uid: String = auth.currentUser?.uid.toString()
    private val pref: Pref by lazy {
        Pref(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJavaOrKotlinBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLevelUser()
    }
    private fun initLevelUser() {
        binding.btnJava.text = "Начать с уровня начинающий"
        binding.btnKotlin.text = "Проверить уровень знания"
        binding.btnJava.setOnClickListener {
            myDataBase.child(uid).child("userlvlQuestionnaire").setValue("Начинающий").addOnSuccessListener {
                findNavController().navigate(R.id.homeScreenFragment)
            }
            pref.onJavaOrKotlinShowed()
        }
        binding.btnKotlin.setOnClickListener {
            findNavController().navigate(R.id.testLvlFragment)
        }
    }
}



