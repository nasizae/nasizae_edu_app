package com.example.edupulse.presentation.ui.home.tasks

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.edupulse.data.model.Users
import com.example.edupulse.data.pref.Pref
import com.example.edupulse.domain.usecase.GetUserDataUseCase
import com.example.edupulse.presentation.ui.home.tasks.adapter.TasksAdapter
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.databinding.AlertdialogTasksBinding
import com.example.nasizae_edu_pulse.databinding.FragmentTasksBinding
import com.example.nasizae_edu_pulse.domain.model.TasksItemModel

class TasksFragment : Fragment(), GetUserDataUseCase.CallBack {
    private lateinit var binding: FragmentTasksBinding
    private lateinit var adapter: TasksAdapter
    private lateinit var list: ArrayList<TasksItemModel>
    private lateinit var alertDialog: AlertDialog
    private val getUserDataUseCase = GetUserDataUseCase()
    private val pref:Pref by lazy {
        Pref(requireContext())
    }
    var position:Int=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLoad()
        initAlertDialog()
        initAdapter()
        initGetUser()
        initListeners()
        initNextUnClocked()
    }

    private fun initNextUnClocked() {
        val data = arguments?.getString("key")
        Log.e("ololo", "OnClickData: $data", )
        if (data.equals("completed")) {
            if (position < list.size - 1 && !list[position + 1].unClocked) {
                list[position + 1] = list[position + 1].copy(unClocked = true)
                adapter.notifyItemChanged(position + 1)
            }
        }
    }

    private fun initListeners() {
        binding.containerChapters.setOnClickListener {
            findNavController().navigate(R.id.chaptersFragment)
        }
        binding.btnAwards.setOnClickListener {
            findNavController().navigate(R.id.awardsFragment)
        }
    }

    private fun initGetUser() {
        getUserDataUseCase.getUser(this)
    }

    private fun initAlertDialog() {
        pref.onOnBoardingShowed()
        val alertDialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogStyle)
        val alertBinding = AlertdialogTasksBinding.inflate(layoutInflater)
        alertDialogBuilder.setView(alertBinding.root)
        alertBinding.btnNext.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog = alertDialogBuilder.create()
    }

    private fun OnClick(possition: Int, countExperience: Int) {
        val health = binding.tvCountHealth.text.toString()
        val bundle =
            bundleOf("position" to possition, "count" to countExperience, "health" to health)
        findNavController().navigate(R.id.gameFragment, bundle)
        position=possition
    }

    private fun initLoad() {
        list = arrayListOf(
            TasksItemModel(1, true),
            TasksItemModel(2, false),
            TasksItemModel(3, false),
            TasksItemModel(4, false),
            TasksItemModel(5, false),
            TasksItemModel(6, false),
            TasksItemModel(7, false),
            TasksItemModel(8, false)
        )


    }

    private fun initAdapter() {
        adapter = TasksAdapter(list, this::OnClick)
        if(!pref.isOnBoardingShow()) {
            alertDialog.show()
        }
        binding.rvTasks.adapter = adapter
    }

    override fun onUserReceived(users: Users) {
        binding.tvUsername.text = users.fullName
        Glide.with(binding.imgUser).load(users.image).into(binding.imgUser)
    }

    override fun onError(error: String) {
        Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
    }

}