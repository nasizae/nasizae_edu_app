package com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.edupulse.domain.usecase.RegistrationUseCase
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.data.model.Chapter
import com.example.nasizae_edu_pulse.data.model.ChapterModel
import com.example.nasizae_edu_pulse.databinding.FragmentChaptersBinding
import com.example.nasizae_edu_pulse.domain.repository.RepositoryImpl
import com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters.adapter.ChaptersAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase

class ChaptersFragment : Fragment() {
    private lateinit var binding: FragmentChaptersBinding
    private val data = FirebaseFirestore.getInstance()
    private val repositoryImpl=RepositoryImpl()
    private val chaptersViewModel=ChaptersViewModel(repositoryImpl)
    private lateinit var adapter: ChaptersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChaptersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGetDataChapters()
        initListeners()


    }
    private fun initListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.homeScreenFragment)
        }
         val auth = FirebaseAuth.getInstance()
         val myDataBase = Firebase.database.getReference(RegistrationUseCase.USER)
        val uid=auth.currentUser?.uid.toString()
        myDataBase.child(uid).child("userlvlQuestionnaire").get().addOnSuccessListener {
            val value=it.value
            if (value == "Начинающий") {
                binding.course.text="Курс 1"
                binding.tvChapterName.text="Уровень Начинающий"
            } else if (value == "Средний") {
                binding.course.text="Курс 2"
                binding.tvChapterName.text="Уровень Средний"
            } else if (value == "Продвинутый") {
                binding.course.text="Курс 3"
                binding.tvChapterName.text="Уровень Продвинутый"
            }
        }
    }

    private fun initGetDataChapters() {
        chaptersViewModel.chapters.observe(viewLifecycleOwner){
          adapter= ChaptersAdapter(it.chapter,this::onIndividualCLick)
            binding.rvChapters.adapter=adapter
        }
    }

    private fun onIndividualCLick() {
        findNavController().navigate(R.id.individualTasksFragment)
    }

    companion object{
        val CHAPTER_COLLECTION="chapters"
        val CHAPTER_DOCUMENT="chpater"
        val CHAPTER_NAME="chaptersName"
        val CHAPTER_COURSE="course"
    }
}
