package com.example.nasizae_edu_pulse.presentation.ui.questionnaire

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.example.edupulse.data.model.UserAnswersQuestionnaire
import com.example.edupulse.data.pref.Pref
import com.example.edupulse.domain.usecase.RegistrationUseCase.Companion.USER
import com.example.nasizae_edu_pulse.R
import com.example.nasizae_edu_pulse.data.model.QuestionnaireModel
import com.example.nasizae_edu_pulse.data.model.QuizQuestionnaire
import com.example.nasizae_edu_pulse.databinding.AlertDialogStartGameBinding
import com.example.nasizae_edu_pulse.databinding.AlertdialogExitAccountBinding
import com.example.nasizae_edu_pulse.databinding.AlertdialogTasksBinding
import com.example.nasizae_edu_pulse.databinding.FragmentQuestionnaireBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class QuestionnaireFragment : Fragment() {
    private lateinit var binding: FragmentQuestionnaireBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var questionnaireModel: QuestionnaireModel
    private var position = 0
    private lateinit var quizQuestionnaire: QuizQuestionnaire
    private val userAnswers = mutableListOf<UserAnswersQuestionnaire>()
    private lateinit var myDataBase: DatabaseReference
    private val auth = FirebaseAuth.getInstance()
    private val uid: String? = auth.currentUser?.uid
    private val pref: Pref by lazy {
        Pref(requireContext())
    }
    private lateinit var alertDialog:AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionnaireBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        createAlertDialog()
        alertDialog.show()

    }

    private fun createAlertDialog() {
            val alertDialogBuilder= AlertDialog.Builder(requireContext(),R.style.CustomAlertDialogStyle)
            val alertDialogBinding= AlertdialogTasksBinding.inflate(layoutInflater)
            alertDialogBuilder.setView(alertDialogBinding.root)
            alertDialog=alertDialogBuilder.create()
            alertDialogBinding.tvTitleWelcome.text="Пожалуйста пройдите маленький опрос"
            alertDialogBinding.tvDesc.visibility=View.GONE
        alertDialogBinding.tvTitleWelcome.gravity=Gravity.CENTER
            alertDialogBinding.btnNext.text="ок"
            alertDialogBinding.btnNext.setOnClickListener {
                alertDialog.dismiss()
            }
        }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (uid == null) {
            Toast.makeText(requireContext(), "Пользователь не аутентифицирован", Toast.LENGTH_SHORT).show()
            return
        }
        myDataBase = FirebaseDatabase.getInstance().getReference(USER).child(uid)
        initDataQuestionnaire()
    }

    private fun initDataQuestionnaire() {
        firestore = FirebaseFirestore.getInstance()
        firestore.collection(QUESTIONNAIRE_COLLECTION).document(QUESTIONNAIRE_DOCUMENT).get()
            .addOnSuccessListener {
                val value = it.toObject(QuizQuestionnaire::class.java)
                if (value != null) {
                    quizQuestionnaire = value
                    showQuestion()
                }
            }
        binding.btnContinue.setOnClickListener {
            checkAnswers()
        }
    }

    private fun showQuestion() {
        if (position < quizQuestionnaire.questions.size) {
            questionnaireModel = quizQuestionnaire.questions[position]
            binding.tvQuestion.text = questionnaireModel.questionText
            binding.rbAnswerFirst.text = questionnaireModel.answers[0]
            binding.rbAnswerSecond.text = questionnaireModel.answers[1]
            binding.rbAnswerThree.text = questionnaireModel.answers[2]
            binding.rbAnswerFour.text = questionnaireModel.answers[3]
            binding.tvCountQuestion.text = "Вопрос ${position + 1}"
        } else {
            saveAnswerToFirebase()
        }
    }

    private fun saveAnswerToFirebase() {
        myDataBase.child("userQuestionnaire").setValue(userAnswers).addOnSuccessListener {
            Toast.makeText(requireContext(), "Данные сохранены", Toast.LENGTH_SHORT).show()
            if(!pref.isJavaOrKotlinShow()) {
                findNavController().navigate(R.id.javaOrKotlinFragment)
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Ошибка при сохрnанении данных", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAnswers() {
        val selectedRadioButtonId = binding.radioGroupFirst.checkedRadioButtonId
        if (selectedRadioButtonId != -1) {
            val selectedRadioButton = view?.findViewById<RadioButton>(selectedRadioButtonId)
            val selectedAnswer = selectedRadioButton?.text.toString()
            val userAnswer = UserAnswersQuestionnaire(
                question = questionnaireModel.questionText,
                userAnswer = selectedAnswer
            )
            userAnswers.add(userAnswer)
            binding.progressQuestion.progress += 1
            binding.radioGroupFirst.clearCheck() // Clear selection for the next question
            position++
            if (position < quizQuestionnaire.questions.size) {
                showQuestion()
            } else {
                saveAnswerToFirebase()
                pref.onQuestionnaireShowed()
            }
        } else {
            Toast.makeText(context, "Пожалуйста, выберите ответ", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val QUESTIONNAIRE_COLLECTION = "Questionnaire"
        const val QUESTIONNAIRE_DOCUMENT = "Survey"
    }
}
