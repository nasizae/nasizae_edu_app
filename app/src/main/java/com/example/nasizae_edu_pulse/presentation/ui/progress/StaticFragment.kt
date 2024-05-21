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
import com.example.nasizae_edu_pulse.databinding.FragmentStaticBinding
import com.example.nasizae_edu_pulse.domain.model.UserStaticModel
import com.example.nasizae_edu_pulse.domain.usecase.GetUserStaticUseCase
import com.example.nasizae_edu_pulse.domain.usecase.SaveDataUserStaticUseCase

class StaticFragment : Fragment(), GetUserDataUseCase.CallBack, GetUserStaticUseCase.Call {
    private lateinit var binding: FragmentStaticBinding
    private var getUserDataUseCase = GetUserDataUseCase()
    private var getUserStaticUseCase = GetUserStaticUseCase()
    var userLvl=1
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

    override fun getData(userStatic: UserStaticModel) {
        var progress=userStatic.progressNumber
        binding.tvProgressText.text = progress.toString()+"/300"
        binding.tvCountExperience.text = userStatic.maxCountProgress.toString()
        binding.progressStaticUser.progress= progress!!
        if(progress>=300){
            progress=0
            userLvl+=1
            binding.tvCountLvl.text=userLvl.toString()
            binding.progressStaticUser.progress=progress
            binding.tvProgressText.text = progress.toString()+"/300"
        }
    }

    override fun error(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

}