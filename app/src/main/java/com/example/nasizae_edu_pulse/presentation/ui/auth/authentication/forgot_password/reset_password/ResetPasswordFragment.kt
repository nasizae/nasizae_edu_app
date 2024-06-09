package com.example.nasizae_edu_pulse.presentation.ui.auth.authentication.forgot_password.reset_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.databinding.FragmentResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordFragment : Fragment() {
    private lateinit var binding: FragmentResetPasswordBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initResetPassword()
    }

    private fun initResetPassword() {
        binding.btnNext.setOnClickListener {
            auth.sendPasswordResetEmail(binding.etNewPassword.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "проверьте почту", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.authenticationFragment)
                }
        }
    }

}