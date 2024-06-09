package com.example.nasizae_edu_pulse.presentation.ui.auth.registration

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.edupulse.domain.usecase.RegistrationUseCase
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var registrationUseCase: RegistrationUseCase
    private var filePath: Uri? = null
    private var password = ""
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
        initListeners()
    }

    private fun initListeners() {
        binding.btnTransitionAuth.setOnClickListener {
            findNavController().navigate(R.id.authenticationFragment)
        }
    }

    private fun initRegisterUsers() {
        binding.imageUser.setOnClickListener {
            selectLoadImage()
        }
        binding.btnRegister.setOnClickListener {
            password = binding.etPassword.text.toString()
            if (filePath != null && password.length >= 8) {
                registrationUseCase =
                    RegistrationUseCase(
                        requireContext(),
                        findNavController(),
                        filePath!!,
                        layoutInflater = layoutInflater
                    )
                registrationUseCase.registration(
                    fullName = binding.etFullName.text.toString(),
                    email = binding.etEmail.text.toString(),
                    password = password,
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "Заполните данные",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                password = s.toString()
                if (password.length < 8) {
                    binding.etPassword.setError("Пароль должен быть не меньше 8 символов", null)
                }
                else if(!password.substring(0,7).matches(Regex("[a-zA-z]+"))){
                    binding.etPassword.setError("Первые 8 символов пароля должен быть из латинских букв", null)
                }

                else {
                    binding.etPassword.setError(null)
                }
            }
        })
    }

    private fun selectLoadImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }


}