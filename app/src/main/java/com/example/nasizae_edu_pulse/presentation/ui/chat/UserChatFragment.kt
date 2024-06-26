package com.example.nasizae_edu_pulse.presentation.ui.chat

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.edupulse.data.model.UserMessageModel
import com.example.edupulse.data.model.Users
import com.example.edupulse.domain.usecase.RegistrationUseCase
import com.example.edupulse.presentation.ui.chat.adapter.MessageAdapter
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.data.model.ThemeMessageModel
import com.example.nasizae_edu_pulse.databinding.FragmentUserChatBinding
import com.example.nasizae_edu_pulse.presentation.ui.chat.theme_message.ThemeMessageFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue
import java.util.Calendar
import java.util.Locale

class UserChatFragment : Fragment() {
    private lateinit var binding: FragmentUserChatBinding
    private lateinit var myRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: MessageAdapter
    private val list = ArrayList<UserMessageModel>()
    private val myDataBase = Firebase.database.getReference(RegistrationUseCase.USER)
    private lateinit var args: UserChatFragmentArgs
    private var key: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        myRef = Firebase.database.getReference(MESSAGE)
        adapter = MessageAdapter(list)
        args = UserChatFragmentArgs.fromBundle(requireArguments())
        key = args.key
        binding.rvMessage.adapter = adapter
        initdata()
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
        val uid = user?.uid.toString()
        myRef.child(uid).child(ThemeMessageFragment.THEME).child(key).child("userMassage")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    snapshot.children.forEach {
                        val message = it.getValue(UserMessageModel::class.java)
                        if (message != null) {
                            list.add(message)
                        }
                        adapter.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Error loading messages", Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }

    private fun addData() {
        val user: FirebaseUser? = auth.currentUser
        val uid: String? = user?.uid
        if (uid != null) {
            myDataBase.child(uid).addListenerForSingleValueEvent(object : ValueEventListener {
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
                    myRef.child(uid).child(ThemeMessageFragment.THEME).child(key)
                        .child("userMassage").push()
                        .setValue(userMessageModel).addOnCompleteListener {
                            if (it.isSuccessful) {
                                list.add(userMessageModel)
                                adapter.notifyDataSetChanged()
                                binding.etMessage.text.clear()
                            }
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    companion object {
        const val MESSAGE = "Message"
    }

}