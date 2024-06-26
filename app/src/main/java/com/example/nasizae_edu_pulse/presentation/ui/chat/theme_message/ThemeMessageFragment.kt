package com.example.nasizae_edu_pulse.presentation.ui.chat.theme_message

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.edupulse.data.model.Users
import com.example.edupulse.domain.usecase.RegistrationUseCase.Companion.USER
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.data.model.ThemeMessageModel
import com.example.nasizae_edu_pulse.databinding.AlertDialogAddThemeBinding
import com.example.nasizae_edu_pulse.databinding.FragmentThameMessageBinding
import com.example.nasizae_edu_pulse.presentation.ui.chat.UserChatFragment.Companion.MESSAGE
import com.example.nasizae_edu_pulse.presentation.ui.chat.theme_message.adapter.ThemeAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ThemeMessageFragment : Fragment() {

    private lateinit var binding: FragmentThameMessageBinding
    private lateinit var alertDialog: AlertDialog
    private val auth = FirebaseAuth.getInstance()
    private val uid = auth.currentUser?.uid.toString()
    private val myDatabase = Firebase.database.getReference(USER)
    private val messageDataBase = Firebase.database.getReference(MESSAGE)
    private val themeAdapter = ThemeAdapter(this::onItemclick)
    private val list = ArrayList<ThemeMessageModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThameMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAddDataTheme()
        initShowThemeMessage()

    }

    private fun initShowThemeMessage() {
        messageDataBase.child(uid).child(THEME).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.children
                list.clear()
                if (value != null) {
                    value.forEach {
                        val theme = it.getValue(ThemeMessageModel::class.java)
                        theme?.let {
                            it.key = it.key ?:it.key
                            list.add(it)
                        }
                            themeAdapter.addData(list)
                            binding.rvThemeMessage.adapter = themeAdapter
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun onItemclick(key:String) {
            findNavController().navigate(ThemeMessageFragmentDirections.actionThemeMessageFragmentToUserChatFragment2(key.toString()))
        Log.e("ololo", "onItemclick: $key", )
    }

    private fun initAddDataTheme() {
        binding.btnAddTheme.setOnClickListener {
            alertDialogShow()
            alertDialog.show()
        }
    }

    private fun alertDialogShow() {
        val alertDialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogStyle)
        val alertDialogBinding = AlertDialogAddThemeBinding.inflate(layoutInflater)
        alertDialogBuilder.setView(alertDialogBinding.root)
        alertDialog = alertDialogBuilder.create()
        alertDialogBinding.btnAdd.setOnClickListener {
            val userThemeMessage = alertDialogBinding.etAddTheme.text.toString()
            saveDataTheme(userThemeMessage)
            alertDialog.dismiss()
        }

    }

    private fun saveDataTheme(userThemeMessage: String) {
        myDatabase.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(Users::class.java)
                if (value != null) {
                    val userImg = value.image
                    if (userImg != null) {
                        val data= messageDataBase.push().child(uid).child(THEME).push()
                        val themeMessage = ThemeMessageModel(userImg, userThemeMessage,data.key)
                        data.setValue(themeMessage)
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    companion object {
        val THEME = "Theme"
    }
}