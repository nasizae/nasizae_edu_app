package com.example.nasizae_edu_pulse.presentation.ui.questionnaire.test

import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.edupulse.domain.usecase.RegistrationUseCase.Companion.USER
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.data.model.Question
import com.example.nasizae_edu_pulse.data.model.Quiz
import com.example.nasizae_edu_pulse.databinding.AlertDialogTestResultBinding
import com.example.nasizae_edu_pulse.databinding.BottomSheetDialogBinding
import com.example.nasizae_edu_pulse.databinding.FragmentTestLvlBinding
import com.example.nasizae_edu_pulse.domain.model.UserStaticModel
import com.example.nasizae_edu_pulse.presentation.ui.home.tasks.tasksgame.GameFragmentDirections
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore

class TestLvlFragment : Fragment() {
    private lateinit var binding: FragmentTestLvlBinding
    private var firestoreDb = FirebaseFirestore.getInstance()
    var position = 0
    private lateinit var question: Question
    private var explanation: String = ""
    private var answer: String = ""
    private lateinit var buttonAnswer: List<Button>
    private var countRightAnswers = 0
    private lateinit var dialog: BottomSheetDialog
    private lateinit var alertdialog: AlertDialog
    private val myDataBase=Firebase.database.getReference(USER)
    private val auth=FirebaseAuth.getInstance()
    private val uid:String=auth.currentUser?.uid.toString()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTestLvlBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initGetData()
    }

    private fun initGetData() {
        firestoreDb.collection("UserTest").document("test").get()
            .addOnSuccessListener {
                val data = it.toObject(Quiz::class.java)
                if (data != null) {
                    if (position < data.questions.size) {
                        question = data.questions[position]
                        binding.btnAnswerFirst.text = question.answers.get(0)
                        binding.btnAnswerSecond.text = question.answers.get(1)
                        binding.btnAnswerThree.text = question.answers.get(2)
                        binding.btnAnswerFour.text = question.answers.get(3)
                        binding.tvQuestionTasks.text = question.questionText
                        explanation = question.explanation.toString()
                        answer = question.correctAnswer.toString()
                    }
                    else {
                        val totalCountRightAnswers = (countRightAnswers * 100) / data.questions.size
                        alertDialogShow(totalCountRightAnswers)
                        alertdialog.show()
                    }
                }
            }
    }

    private fun alertDialogShow(totalCountRightAnswers: Int) {
        val alertDialogBuilder =
            AlertDialog.Builder(requireContext(), R.style.CustomAlertDialogStyle)
        val alertDialogBinding = AlertDialogTestResultBinding.inflate(layoutInflater)
        alertDialogBuilder.setView(alertDialogBinding.root)
        alertdialog = alertDialogBuilder.create()
        if (totalCountRightAnswers < 45) {
            alertDialogBinding.numberLvl.text="Начинающий"
            alertDialogBinding.numberExp.text=totalCountRightAnswers.toString()+"%"
            myDataBase.child(uid).child("userlvlQuestionnaire").setValue(alertDialogBinding.numberLvl.text.toString())

        } else if (totalCountRightAnswers < 75) {
            alertDialogBinding.numberLvl.text="Средний"
            alertDialogBinding.numberExp.text=totalCountRightAnswers.toString()+"%"
            myDataBase.child(uid).child("userlvlQuestionnaire").setValue(alertDialogBinding.numberLvl.text.toString())
        } else {
            alertDialogBinding.numberLvl.text="Продвинутый"
            alertDialogBinding.numberExp.text=totalCountRightAnswers.toString()+"%"
            myDataBase.child(uid).child("userlvlQuestionnaire").setValue(alertDialogBinding.numberLvl.text.toString())
        }
        alertDialogBinding.btnStart.setOnClickListener {
            findNavController().navigate(R.id.homeScreenFragment)
            alertdialog.dismiss()
        }
    }

    private fun initListeners() {
        buttonAnswer = listOf(
            binding.btnAnswerFirst,
            binding.btnAnswerSecond,
            binding.btnAnswerThree,
            binding.btnAnswerFour
        )

        buttonAnswer.forEach { button ->
            button.setOnClickListener {
                clickButton(button)
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnCheck.setOnClickListener {
            checkAnswer()
        }
    }

    private fun checkAnswer() {
        val selectButton = buttonAnswer.find {
            it.background.constantState == ContextCompat.getDrawable(
                requireContext(),
                R.drawable.bg_active_btn_answer
            )?.constantState
        }
        if (selectButton != null) {
            showBottomSheet(selectButton)
        } else {
            Toast.makeText(requireContext(), "Выберите ответ", Toast.LENGTH_SHORT).show()
        }
        buttonAnswer.forEach {
            it.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_answers))
        }
    }

    private fun showBottomSheet(selectButton: Button) {
        val dialogBinding = BottomSheetDialogBinding.inflate(layoutInflater)
        dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        dialog.setContentView(dialogBinding.root)
        dialog.show()
        if (selectButton.text.toString().equals(answer)) {
            var media = MediaPlayer.create(requireContext(), R.raw.corect_answer)
            media.let { if (!it.isPlaying) it.start() }
            dialogBinding.container.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_bottom_sheet
                )
            )
            dialogBinding.tvAnswer.text = "Пояснения:\n" + explanation
            dialogBinding.tvAnswer.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            countRightAnswers++

        } else {
            var media = MediaPlayer.create(requireContext(), R.raw.ne_pravilno)
            media.let { if (!it.isPlaying) it.start() }
            dialogBinding.tvTextRight.text = "Не правильно"
            dialogBinding.tvAnswer.text = selectButton.text.toString()
            dialogBinding.tvAnswer.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            dialogBinding.tvWrongText.text = "Правильный"
            dialogBinding.tvRightAnswers.text = answer
            dialogBinding.tvRightAnswers.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )
            dialogBinding.iconRight.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_wrong_answer
                )
            )
            dialogBinding.container.setBackground(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_wrong_bottom_sheet
                )
            )
        }
        dialogBinding.btnNext.setOnClickListener {
            position++
            binding.progressCountAnswer.progress += 1
            dialog.dismiss()
            initGetData()
        }
    }

    private fun clickButton(selectButton: Button) {
        buttonAnswer.forEach {
            it.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.bg_answers))
        }
        selectButton.setBackground(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.bg_active_btn_answer
            )
        )
    }

}