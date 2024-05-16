package com.example.edupulse.presentation.ui.auth

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.edupulse.domain.usecase.RegistrationUseCase
import com.example.nasizae_edu_pulse.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var filePath: Uri
    private lateinit var registrationUseCase: RegistrationUseCase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRegisterUsers()
    }

    private fun initRegisterUsers() {
        binding.imageUser.setOnClickListener {
            selectLoadImage()
        }
        binding.btnRegister.setOnClickListener {
            registrationUseCase = RegistrationUseCase(requireContext(), findNavController(), filePath)
            registrationUseCase.registration(
                fullName = binding.etFullName.text.toString(),
                email = binding.etEmail.text.toString(),
                password = binding.etPassword.text.toString()
            )
        }

    }

    private fun selectLoadImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK &&
                result.data != null && result.data?.data != null
            ) {
                filePath = result.data!!.data!!
                try {
                    val bitmap: Bitmap
                    bitmap = MediaStore.Images.Media.getBitmap(
                        requireContext().contentResolver,
                        filePath
                    )
                    binding.imageUser.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }


}