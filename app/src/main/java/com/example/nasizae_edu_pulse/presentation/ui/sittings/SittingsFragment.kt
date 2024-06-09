package com.example.edupulse.presentation.ui.sittings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.edupulse.data.model.Users
import com.example.edupulse.domain.usecase.GetUserDataUseCase
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.databinding.AlertdialogExitAccountBinding
import com.example.nasizae_edu_pulse.databinding.FragmentSittingsBinding
import com.google.firebase.auth.FirebaseAuth

class SittingsFragment : Fragment(),GetUserDataUseCase.CallBack{

    private lateinit var binding: FragmentSittingsBinding
    private lateinit var alertDialog: AlertDialog
    private val getUserDataUseCase=GetUserDataUseCase()
    private val auth=FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSittingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        getProfile()
    }

    private fun getProfile() {
       getUserDataUseCase.getUser(this)
    }
    private fun initListeners() {
        binding.btnExit.setOnClickListener {
            initAlertDialogExit()

        }
        binding.btnEdit.setOnClickListener {
            findNavController().navigate(R.id.editProfileFragment)
        }
    }

    private fun initAlertDialogExit() {
        val alertDialogBuilder=AlertDialog.Builder(requireContext(),R.style.CustomAlertDialogStyle)
        val alertDialogBinding= AlertdialogExitAccountBinding.inflate(layoutInflater)
        alertDialogBuilder.setView(alertDialogBinding.root)
        alertDialog=alertDialogBuilder.create()
        alertDialogBinding.btnYes.setOnClickListener {
            auth.signOut()
            findNavController().navigate(R.id.authenticationFragment)
            alertDialog.dismiss()
        }
        alertDialogBinding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
    override fun onUserReceived(users: Users) {
        binding.tvEmail.text=users.email
        binding.tvUsername.text=users.fullName
        Glide.with(binding.imgProfile).load(users.image).into(binding.imgProfile)
    }

    override fun onError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }
}