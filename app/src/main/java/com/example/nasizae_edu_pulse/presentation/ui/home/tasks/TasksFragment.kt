package com.example.edupulse.presentation.ui.home.tasks

import android.os.Bundle
import android.os.CountDownTimer
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
import com.example.edupulse.domain.usecase.RegistrationUseCase.Companion.USER
import com.example.edupulse.presentation.ui.home.tasks.adapter.TasksAdapter
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.data.model.Chapter
import com.example.nasizae_edu_pulse.data.model.ChapterModel
import com.example.nasizae_edu_pulse.data.model.Question
import com.example.nasizae_edu_pulse.data.model.Quiz
import com.example.nasizae_edu_pulse.databinding.AlertDialogStartGameBinding
import com.example.nasizae_edu_pulse.databinding.AlertdialogTasksBinding
import com.example.nasizae_edu_pulse.databinding.FragmentTasksBinding
import com.example.nasizae_edu_pulse.domain.model.TasksItemModel
import com.example.nasizae_edu_pulse.domain.model.UserDataStaticTasks
import com.example.nasizae_edu_pulse.domain.model.UserStaticModel
import com.example.nasizae_edu_pulse.domain.repository.RepositoryImpl
import com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters.ChaptersFragment.Companion.CHAPTER_COLLECTION
import com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters.ChaptersFragment.Companion.CHAPTER_COURSE
import com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters.ChaptersFragment.Companion.CHAPTER_DOCUMENT
import com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters.ChaptersFragment.Companion.CHAPTER_NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class TasksFragment : Fragment(), GetUserDataUseCase.CallBack {
    private lateinit var binding: FragmentTasksBinding
    private lateinit var adapter: TasksAdapter
    private lateinit var list: ArrayList<TasksItemModel>
    private lateinit var alertDialog: AlertDialog
    private var firestoreDb = FirebaseFirestore.getInstance()
    private val myDataBase = Firebase.database.getReference(USER)
    private val auth = FirebaseAuth.getInstance()
    private val repositoryImpl = RepositoryImpl()
    private val tasksViewModel = TasksViewModel(repositoryImpl)
    var userLvl: Int = 1
    private var health: Int = 5
    private val pref: Pref by lazy {
        Pref(requireContext())
    }
    var position: Int = 0
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
        initGetHealth()
        if (!pref.alertDialogShowed()) {
            alertDialog.show()
        }
        initLoadLvl()
        initGetCourseChapter()
    }
    private fun initLoadLvl() {
        val uid = auth.currentUser?.uid.toString()
        myDataBase.child(uid).child("static").child("static_in_lvl").get().addOnSuccessListener {
            var position = it.value as? Long
            if (position != null) {
                for (i in 0..position.toInt()) {
                    if (i < list.size) {
                        list[i] = list[i].copy(unClocked = true)
                    }
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun initGetHealth() {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()
        myDataBase.child(uid).child("static").child("userHealth")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(UserStaticModel::class.java)
                    if (value?.health != null) {
                        health = value.health
                        binding.tvCountHealth.text = value.health.toString()
                        if (value.health < 5) {
                            startTimerHealth(uid, value.health)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun startTimerHealth(uid: String, health: Int) {
        val time: Long = 300000L
        object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                if (health == 0) {
                    binding.tvCountHealth.visibility = View.GONE
                    binding.tvTimer.visibility = View.VISIBLE
                    binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
                } else {
                    binding.tvCountHealth.visibility = View.VISIBLE
                    binding.tvTimer.visibility = View.GONE
                }
            }

            override fun onFinish() {
                val newHealth = health + 1
                val userStaticModel = UserStaticModel(health = newHealth)
                myDataBase.child(uid).child("static").child("userHealth").setValue(userStaticModel)
            }
        }.start()
    }

    private fun initGetCourseChapter() {
        val uid = auth.currentUser?.uid.toString()
        myDataBase.child(uid).child("userlvlQuestionnaire").get().addOnSuccessListener {
            val value = it.value
            var data = ""
            if (value == "Начинающий") {
                data = "chapterFirstLVL"
            } else if (value == "Средний") {
                data = "chapterAverageLVL"
            } else if (value == "Продвинутый") {
                data = "chapterAdvanceLVL"
            }
            firestoreDb.collection(CHAPTER_COLLECTION).document(data).get()
                .addOnSuccessListener {
                    val chapter = it.toObject(Chapter::class.java)
                    if (chapter != null) {
                        binding.tvChapterTasks.text = chapter.chapter.get(0).chapterName
                        binding.course.text = chapter.chapter.get(0).course

                    }
                    val userStaticModel = UserDataStaticTasks(
                        countUserLvl = userLvl,
                        nameThemeWork = "${binding.course.text} ${binding.tvChapterTasks.text}",

                        )
                    myDataBase.child(uid).child("static").child("static_in_tasks")
                        .setValue(userStaticModel)
                }
        }
    }

    private fun initNextUnClocked() {
        val data = arguments?.getString("key")
        if (data.equals("completed")) {
            if (position < list.size - 1 && !list[position + 1].unClocked) {
                list[position + 1] = list[position + 1].copy(unClocked = true)
                adapter.notifyItemChanged(position + 1)
                position += 1
                initSaveLvl(position)
                userLvl = userLvl.plus(position)
            }
        }
    }

    private fun initSaveLvl(position: Int) {
        val uid = auth.currentUser?.uid.toString()
        myDataBase.child(uid).child("static").child("static_in_lvl")
            .setValue(position)
    }

    private fun initListeners() {
        binding.containerChapters.setOnClickListener {
            findNavController().navigate(R.id.chaptersFragment)
        }
    }

    private fun initGetUser() {
//        getUserDataUseCase.getUser(this)
        tasksViewModel.users.observe(viewLifecycleOwner) { users ->
            binding.tvUsername.text = users.fullName
            Glide.with(binding.imgUser).load(users.image).into(binding.imgUser)
        }
        tasksViewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                binding.loading.root.visibility = View.VISIBLE
            } else {
                binding.loading.root.visibility = View.GONE
            }
        }

    }

    private fun initAlertDialog() {
        val alertDialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogStyle)
        val alertBinding = AlertdialogTasksBinding.inflate(layoutInflater)
        alertDialogBuilder.setView(alertBinding.root)
        alertBinding.btnNext.setOnClickListener {
            alertDialog.dismiss()
            pref.alertDialogShow()
        }
        alertDialog = alertDialogBuilder.create()
    }

    private fun OnClick(possition: Int, countExperience: Int) {
        if (health > 0) {
            alertDialogStart(possition, countExperience)
            alertDialog.show()
        } else {
            Toast.makeText(
                requireContext(),
                "К сожалению у вас 0 жизней наберитесь терпению и дождитесь пока у вас пополниться жизнь",
                Toast.LENGTH_SHORT
            ).show()
        }
        position = possition
    }

    private fun alertDialogStart(possition: Int, countExperience: Int) {
        val alertDialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogStyle)
        val alertDialogBinding = AlertDialogStartGameBinding.inflate(layoutInflater)
        alertDialogBuilder.setView(alertDialogBinding.root)
        alertDialogBinding.btnStart.setOnClickListener {
            val bundle =
                bundleOf("position" to possition, "count" to countExperience)
            findNavController().navigate(R.id.gameFragment, bundle)
            alertDialog.dismiss()
        }
        alertDialogBinding.numberTasks.text = (possition + 1).toString()
        alertDialogBinding.numberExp.text = countExperience.toString()
        alertDialog = alertDialogBuilder.create()
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