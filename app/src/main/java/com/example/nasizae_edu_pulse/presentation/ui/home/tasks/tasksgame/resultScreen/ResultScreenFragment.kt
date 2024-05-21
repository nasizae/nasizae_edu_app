package com.example.nasizae_edu_pulse.presentation.ui.home.tasks.tasksgame.resultScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.edupulse.domain.usecase.RegistrationUseCase.Companion.USER
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.databinding.FragmentResultScreenBinding
import com.example.nasizae_edu_pulse.domain.model.UserStaticModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ResultScreenFragment : Fragment() {
    private lateinit var binding: FragmentResultScreenBinding
    private lateinit var args: ResultScreenFragmentArgs
    private var maxExperience=0
    private val auth=FirebaseAuth.getInstance()
    private val myDataBase=Firebase.database.getReference(USER)
    private var progress=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initData()
    }

    private fun initData() {
        args=ResultScreenFragmentArgs.fromBundle(requireArguments())
        binding.time.text = args.time
        binding.tvCountExperience.text = args.experience.toString()
        binding.tvCountRightAnswers.text = args.countRightAnswers.toString()+"%"
        saveData(args)
    }

    private fun saveData(args: ResultScreenFragmentArgs) {
        maxExperience+=args.experience
        progress+=args.experience
        val user:FirebaseUser?=auth.currentUser
        val uid:String?=user?.uid
        if(uid!=null){
            if(progress>=300) {
                progress=0

            }
            val userStatic = UserStaticModel(
                maxCountProgress = maxExperience,
                progressNumber = progress
            )
            myDataBase.child(uid).child("static").setValue(userStatic)
        }
    }

    private fun initListeners() {
        binding.btnNext.setOnClickListener {
            val completed="completed"
            findNavController().navigate(R.id.homefragment, bundleOf("key" to completed))
        }
    }
}