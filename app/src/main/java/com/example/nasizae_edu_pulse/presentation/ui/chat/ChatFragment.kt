package com.example.edupulse.presentation.ui.chat

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.edupulse.data.model.UserMessageModel
import com.example.edupulse.data.model.Users
import com.example.edupulse.domain.usecase.RegistrationUseCase.Companion.USER
import com.example.edupulse.presentation.ui.chat.adapter.MessageAdapter
import com.example.nasizae_edu_pulse.databinding.FragmentChatBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var myRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: MessageAdapter
    private val list = ArrayList<UserMessageModel>()
    private val myDataBase = Firebase.database.getReference(USER)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        myRef = Firebase.database.getReference(MESSAGE)
        initdata()
        adapter = MessageAdapter(list)
        binding.rvMessage.adapter = adapter

    }


    private fun initdata() {
        val user: FirebaseUser? = auth.currentUser
        binding.btnSend.setOnClickListener {
            if (user != null && !TextUtils.isEmpty(binding.etMessage.text.toString())) {
                addData()
            } else {
                Toast.makeText(requireContext(), "Пустое поле", Toast.LENGTH_SHORT).show()
            }
        }
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.children
                list.clear()
                value.forEach {
                    val user = it.getValue(UserMessageModel::class.java)
                    if (user != null) {
                        list.add(user)
                    }
                }
                binding.etMessage.text = null
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun addData() {
        val user: FirebaseUser? = auth.currentUser
        val uid: String? = user?.uid
        if (uid != null) {
            myDataBase.child(uid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(Users::class.java)
                    val calendar = Calendar.getInstance()
                    val simpleTime = SimpleDateFormat("HH:mm", Locale.getDefault())
                    val simpleDate = SimpleDateFormat("dd MMMM", Locale("ru"))
                    val userName = value?.fullName.toString()
                    val message = binding.etMessage.text.toString()
                    val time = simpleTime.format(calendar.time)
                    val imageUser = value?.image.toString()
                    val date = simpleDate.format(calendar.time)
                    val userMessageModel =
                        UserMessageModel(userName, message, time, imageUser, date)
                    myRef.push().setValue(userMessageModel)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                }

            })
        }

    }

    companion object {
        const val MESSAGE = "Message"
    }
}