package com.example.nasizae_edu_pulse.presentation.ui.home.tasks.tasksgame

import android.media.MediaParser
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.edupulse.domain.usecase.RegistrationUseCase.Companion.USER
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.data.model.Question
import com.example.nasizae_edu_pulse.data.model.Quiz
import com.example.nasizae_edu_pulse.databinding.BottomSheetDialogBinding
import com.example.nasizae_edu_pulse.databinding.FragmentGameBinding
import com.example.nasizae_edu_pulse.domain.model.UserStaticModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private val firestoreDb = FirebaseFirestore.getInstance()
    private lateinit var buttonAnswer: List<Button>
    private var answer: String = ""
    private var position: Int = 0
    private var health: Int? = null
    private var countQuestion = 1
    private lateinit var question: Question
    private var questionStartTime: Long = 0L
    private lateinit var dialog: BottomSheetDialog
    private var countRightAnswers = 0
    private var time: String = ""
    private var explanation: String = ""
    private val myDataBase = Firebase.database.getReference(USER)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initGetData()
        questionStartTime = System.currentTimeMillis()
        initGetHealthUser()
    }

    private fun initGetHealthUser() {
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()
        myDataBase.child(uid).child("static").child("userHealth")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.getValue(UserStaticModel::class.java)
                    if (value != null) {
                        binding.tvCountHealth.text = value.health.toString()
                        health = value.health
                    }
                }

                override fun onCancelled(error: DatabaseError) {}

            })
    }

    private fun stopQuizTimer() {
        val quizEndTime = System.currentTimeMillis()
        val quizTimeInMilliseconds = quizEndTime - questionStartTime
        val quizTimeInSeconds = quizTimeInMilliseconds / 1000
        val minutes = quizTimeInSeconds / 60
        val seconds = quizTimeInSeconds % 60
        time = "$minutes:$seconds"
    }

    private fun initGetData() {
        val countExperience = arguments?.getInt("count")
        val quizPosition = arguments?.getInt("position")
        val auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid.toString()
        myDataBase.child(uid).child("userlvlQuestionnaire").get().addOnSuccessListener {
            val value = it.value
            var data = ""
            if (value == "Начинающий") {
                data = "QuestionsFirst"
            } else if (value == "Средний") {
                data = "QuestionsAverage"
            } else if (value == "Продвинутый") {
                data = "QuestionsAdvance"
            }
            firestoreDb.collection(data + quizPosition)
                .document(data + quizPosition).get()
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
                        } else {
                            val totalCountRightAnswers =
                                (countRightAnswers * 100) / data.questions.size
                            stopQuizTimer()
                            if (countExperience != null) {
                                findNavController().navigate(
                                    GameFragmentDirections.actionGameFragmentToResultScreenFragment(
                                        time,
                                        countExperience,
                                        totalCountRightAnswers,
                                        quizPosition!!
                                    )
                                )
                            }
                        }
                    }
                }
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
           var media=MediaPlayer.create(requireContext(),R.raw.corect_answer)
            media.let { if(!it.isPlaying) it.start() }
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
            var media=MediaPlayer.create(requireContext(),R.raw.ne_pravilno)
            media.let { if(!it.isPlaying) it.start() }
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
            health = health!! - 1
            val auth = FirebaseAuth.getInstance()
            val uid = auth.currentUser?.uid.toString()
            val userStaticModel = UserStaticModel(health = health!!)
            myDataBase.child(uid).child("static").child("userHealth").setValue(userStaticModel)
            if (health == 0) {
                findNavController().navigate(R.id.homeScreenFragment)
            }
        }
        dialogBinding.btnNext.setOnClickListener {
            position++
            countQuestion++
            binding.tvCountLvl.text = countQuestion.toString()
            binding.progressCountAnswer.progress +=1
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