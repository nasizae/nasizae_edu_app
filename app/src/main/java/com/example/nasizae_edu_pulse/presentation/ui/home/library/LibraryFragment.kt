package com.example.edupulse.presentation.ui.home.library

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import com.example.edupulse.presentation.ui.home.library.adapter.LibraryAdapter
import com.example.nasizae_edu_pulse.App
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.data.model.PersonalLibraryModel
import com.example.nasizae_edu_pulse.databinding.AlertdialogSaveLibraryBinding
import com.example.nasizae_edu_pulse.databinding.FragmentLibraryBinding
import com.google.firebase.firestore.FirebaseFirestore

class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding
    private val firestoreDb = FirebaseFirestore.getInstance()
    private lateinit var links: List<String>
    private lateinit var alertDialog: AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestoreDb.collection("Library").document("lybrarys").get().addOnSuccessListener {
            val data = it.get("libraryName") as? List<String>
            links = (it.get("links") as? List<String>)!!
            if (data != null) {
                val adapter = LibraryAdapter(data, this::onItemClick, this::onItemLongClick)
                binding.rvLibrary.adapter = adapter
            }
        }
    }

    private fun onItemLongClick(position: Int, libraryName: String): Boolean {
        createAlertDialog(position, libraryName)
        alertDialog.show()
        return true
    }

    private fun createAlertDialog(position: Int, libraryName: String) {
        val alertDialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogStyle)
        val alertDialogBinding = AlertdialogSaveLibraryBinding.inflate(layoutInflater)
        alertDialogBuilder.setView(alertDialogBinding.root)
        alertDialogBinding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialogBinding.btnYes.setOnClickListener {
            App.personalLibraryDataBase.libraryDao()
                .insert(PersonalLibraryModel(position = position, libraryName = libraryName))
            alertDialog.dismiss()
        }
        alertDialog=alertDialogBuilder.create()
    }

    private fun onItemClick(position: Int) {
        val intent = Intent(Intent.ACTION_VIEW, links.get(position).toUri())
        startActivity(intent)
    }

}