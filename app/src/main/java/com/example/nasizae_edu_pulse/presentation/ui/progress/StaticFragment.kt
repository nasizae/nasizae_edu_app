package com.example.edupulse.presentation.ui.progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.edupulse.data.model.Users
import com.example.edupulse.domain.usecase.GetUserDataUseCase
import com.example.edupulse.domain.usecase.RegistrationUseCase.Companion.USER
import com.example.nasizae_edu_pulse.databinding.FragmentStaticBinding
import com.example.nasizae_edu_pulse.domain.model.UserDataStaticResult
import com.example.nasizae_edu_pulse.domain.model.UserDataStaticTasks
import com.example.nasizae_edu_pulse.domain.model.userResult
import com.example.nasizae_edu_pulse.domain.usecase.GetUserStaticUseCase
import com.example.nasizae_edu_pulse.presentation.ui.progress.adapter.HistoryAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class StaticFragment : Fragment(), GetUserDataUseCase.CallBack, GetUserStaticUseCase.Call {
    private lateinit var binding: FragmentStaticBinding
    private var getUserDataUseCase = GetUserDataUseCase()
    private var getUserStaticUseCase = GetUserStaticUseCase()
    private val myDataBase = Firebase.database.getReference(USER)
    private val auth = FirebaseAuth.getInstance()
    private val uid = auth.currentUser?.uid.toString()
    private val adapter = HistoryAdapter()
    var userLvl = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStaticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProfileUser()
        initDataStatic()
        initHistory()
    }

    private fun initHistory() {
        val list = ArrayList<userResult>()
        myDataBase.child(uid).child("static").child("static_in_user_result")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value=snapshot.children
                    value.forEach {
                        val data=it.getValue(userResult::class.java)
                        if(data!=null){
                            list.add(data)
                        }
                    }
                    adapter.addData(list)
                    binding.rvHistory.adapter=adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }


    private fun initDataStatic() {
        getUserStaticUseCase.getUserStatic(this)
    }

    private fun initProfileUser() {
        getUserDataUseCase.getUser(this)
    }

    override fun onUserReceived(users: Users) {
        binding.tvUsername.text = users.fullName
        binding.tvUserEmail.text = users.email
        Glide.with(binding.imgUser).load(users.image).into(binding.imgUser)
    }

    override fun onError(error: String) {
        Toast.makeText(requireContext(), "ошибка сети", Toast.LENGTH_SHORT).show()
    }

    override fun getDataStaticTasks(userStaticTasks: UserDataStaticTasks) {
        binding.tvNameCourse.text = userStaticTasks.nameThemeWork
        binding.tvStaticLvl.text = userStaticTasks.countUserLvl.toString()

    }

    override fun getDataStaticResult(userStaticResult: UserDataStaticResult) {
        var progress = userStaticResult.progressNumber
        binding.tvProgressText.text = progress.toString() + "/300"
        binding.tvCountExperience.text = userStaticResult.progressNumber.toString()
        binding.progressStaticUser.progress = progress
        if (progress >= 300) {
            progress = 0
            userLvl += 1
            binding.progressStaticUser.progress = progress
            binding.tvProgressText.text = progress.toString() + "/300"
            binding.tvCountLvl.text = userLvl.toString()
        }
    }

    override fun error(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

}