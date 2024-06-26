package com.example.nasizae_edu_pulse.presentation.ui.home.tasks.chapters.individual

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.data.model.PersonTasksModel
import com.example.nasizae_edu_pulse.databinding.AlertDialogTestResultBinding
import com.example.nasizae_edu_pulse.databinding.FragmentIndividualTasksBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log
import kotlin.random.Random

class IndividualTasksFragment : Fragment() {

    private lateinit var binding: FragmentIndividualTasksBinding
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var buttonAnswer: List<Button>
    private lateinit var answer: List<Button>
    var position = 0
    private lateinit var alertDialog: AlertDialog
    var userAnswer=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIndividualTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTasks()

    }

    private fun initTasks() {
        firestore.collection("Individual").document("tasks").get().addOnSuccessListener {
            val value = it.toObject(PersonTasksModel::class.java)
            binding.tvTasks.text = value?.tasks
            binding.btnAnswerFirst.text = value?.correctAnswerFirst
            binding.btnAnswerSecond.text = value?.correctAnswerSecond
            binding.btnAnswerThree.text = value?.correctAnswerThree
            binding.btnAnswerFour.text = value?.correctAnswerFour
            binding.btnAnswerFive.text = value?.correctAnswerFive
            binding.btnAnswerSix.text = value?.correctAnswerSix
            binding.btnAnswerSeven.text = value?.correctAnswerSeven
            userAnswer=value?.answer.toString()
            buttonAnswer = listOf(
                binding.btnAnswerFirst,
                binding.btnAnswerSecond,
                binding.btnAnswerThree,
                binding.btnAnswerFour,
                binding.btnAnswerFive,
                binding.btnAnswerSix,
                binding.btnAnswerSeven,
            )
            answer = listOf(
                binding.tvAnswerFirst,
                binding.tvAnswerSecond,
                binding.tvAnswerThree,
                binding.tvAnswerFour,
                binding.tvAnswerFive,
                binding.tvAnswerSix,
                binding.tvAnswerSeven,
            )
            buttonAnswer.forEach { button ->
                button.setOnClickListener {
                    button.visibility = View.INVISIBLE
                    answer[position].text = button.text.toString()
                    answer[position].visibility = View.VISIBLE
                    position += 1
                }
            }
            binding.btnCheck.setOnClickListener {
                checkAnswer(value)
            }

        }
    }

    private fun checkAnswer(value: PersonTasksModel?) {
        val correctAnswers = listOf(
            value?.answerFirst,
            value?.answerSecond,
            value?.answerThree,
            value?.answerFour,
            value?.answerFive,
            value?.answerSix,
            value?.answerSeven
        ).filterNotNull() // Фильтруем null значения, если они есть

        if (answer.size != correctAnswers.size) {
            Toast.makeText(requireContext(), "Количество ответов не совпадает с количеством правильных ответов", Toast.LENGTH_SHORT).show()
            return
        }

        val incorrectAnswers = mutableListOf<String>()
        answer.indices.forEach { index ->
            val userAnswer = answer[index].text.toString()
            if (index < correctAnswers.size) {
                if (userAnswer != correctAnswers[index]) {
                    incorrectAnswers.add("Ответ ${index + 1}: $userAnswer (правильный: ${correctAnswers[index]})")
                }
            } else {
                // Если индекс превышает количество правильных ответов, добавить сообщение об ошибке
                incorrectAnswers.add("Ответ ${index + 1}: $userAnswer (нет правильного ответа)")
            }
        }

        if (incorrectAnswers.isEmpty()) {
            alertDialog()
            alertDialog.show()
        } else {
            val errorMessage = "Ошибки:\n${incorrectAnswers.joinToString("\n")}"
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            Log.e("checkAnswer", errorMessage)

            // Возвращаем кнопки в исходное состояние
            answer.forEach { it.visibility = View.INVISIBLE }
            buttonAnswer.forEach { it.visibility = View.VISIBLE }
            position = 0 // Сбрасываем позицию
        }
    }

    private fun alertDialog() {
        val alertDialogBuilder=AlertDialog.Builder(requireContext(),R.style.CustomAlertDialogStyle)
        val alertDialogBinding=AlertDialogTestResultBinding.inflate(layoutInflater)
        alertDialogBuilder.setView(alertDialogBinding.root)
        alertDialog=alertDialogBuilder.create()
        alertDialogBinding.tvExp.visibility=View.GONE
        alertDialogBinding.tvLvl.visibility=View.GONE
        alertDialogBinding.btnStart.text="Далее"
        alertDialogBinding.numberLvl.text="Превосходно!"
        alertDialogBinding.numberExp.text=userAnswer
        alertDialogBinding.numberExp.textSize= 14F

        alertDialogBinding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.homeScreenFragment)
            alertDialog.dismiss()
        }
    }


}
