package com.example.edupulse.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.edupulse.domain.usecase.LoginUseCase
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.databinding.FragmentAuthenticationBinding
import com.google.firebase.auth.FirebaseAuth

class AuthenticationFragment : Fragment() {
    private lateinit var binding: FragmentAuthenticationBinding
    private lateinit var loginUseCase: LoginUseCase
    private val auth=FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthenticationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSignInUsers()
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser!=null){
            findNavController().navigate(R.id.homeScreenFragment)
        }
        else{
            Toast.makeText(requireContext(), "Сделайте вход", Toast.LENGTH_SHORT).show()
        }

    }

    private fun initSignInUsers() {
        loginUseCase=LoginUseCase(requireContext(),findNavController())
        binding.btnTransitionRegistration.setOnClickListener {
            findNavController().navigate(R.id.registrationFragment)
        }
        binding.btnSignIn.setOnClickListener {
            loginUseCase.login(binding.etEmail.text.toString(),
                binding.etPassword.text.toString())
        }

    }
}